package nl.mmeiboom.adventofcode.y2025.day10

import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status
import nl.mmeiboom.adventofcode.lib.Pathfinding.search
import nl.mmeiboom.adventofcode.lib.Solution
import nl.mmeiboom.adventofcode.y2025.day10.Factory.Machine

/**
 * This day's part 2 has been solved using example code and Z3 library for solving a system of equations.
 * I by no means claim to fully grasp how to solve this from scratch.
 */
class Factory(fileName: String?) : Solution<Machine, Long>(fileName) {


    override fun parse(line: String): Machine {
        val machineRegex = Regex("""^\[([.#]+)] ([(,)\s\d]*) \{([\d,]+)}$""")
        val (lights, buttons, joltages) = machineRegex.matchEntire(line)!!.destructured
        return Machine(
            buttons = buttons
                .split(' ')
                .map { it.removeSurrounding("(", ")") }
                .map { it.split(',').map(String::toInt).toSet() },
            targetLights = lights.map { it == '#' },
            targetJoltages = joltages.split(',').map(String::toInt),
        )
    }

    override fun solve1(data: List<Machine>): Long {
        return data.sumOf { solveLights(it) }.toLong()
    }

    override fun solve2(data: List<Machine>): Long {
        return data.sumOf { solveJoltages(it) }.toLong()
    }

    private fun solveLights(config: Machine) =
        search(
            start = List(config.targetLights.size) { false },
            goalFunction = { state -> state == config.targetLights },
            neighbours = { state ->
                config.buttons
                    .map { flips -> state.mapIndexed { index, on -> if (index in flips) !on else on } }
                    .map { it to 1 }
            },
        )?.cost ?: error("No solution found for machine: $config")

    private fun solveJoltages(config: Machine): Int = Context().use { ctx ->
        val solver = ctx.mkOptimize()
        val zero = ctx.mkInt(0)

        // Counts number of presses for each button, and ensures it is positive.
        val buttons = config.buttons.indices
            .map { ctx.mkIntConst("button#$it") }
            .onEach { button -> solver.Add(ctx.mkGe(button, zero)) }
            .toTypedArray()

        // For each joltage counter, require that the sum of presses of all buttons that increment it is equal to the
        // target value specified in the config.
        config.targetJoltages.forEachIndexed { counter, targetValue ->
            val buttonsThatIncrement = config.buttons
                .withIndex()
                .filter { (_, counters) -> counter in counters }
                .map { buttons[it.index] }
                .toTypedArray()
            val target = ctx.mkInt(targetValue)
            val sumOfPresses = ctx.mkAdd(*buttonsThatIncrement) as IntExpr
            solver.Add(ctx.mkEq(sumOfPresses, target))
        }

        // Describe that the presses (solution answer) is the sum of all individual button presses, and should be as
        // low as possible.
        val presses = ctx.mkIntConst("presses")
        solver.Add(ctx.mkEq(presses, ctx.mkAdd(*buttons)))
        solver.MkMinimize(presses)

        // Find solution and return.
        if (solver.Check() != Status.SATISFIABLE) error("No solution found for machine: $config.")
        solver.getModel().evaluate(presses, false).let { it as IntNum }.int
    }

    data class Machine(
        val buttons: List<Set<Int>>,
        val targetLights: List<Boolean>,
        val targetJoltages: List<Int>,
    )

}

