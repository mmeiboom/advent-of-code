package nl.mmeiboom.adventofcode.y2025.day10

import nl.mmeiboom.adventofcode.lib.Solution

class Factory(fileName: String?) : Solution<TheInput, Long>(fileName) {

    override fun parse(line: String): TheInput {
        val parseRegex = "\\[(.*)\\]( \\(.*\\)+) \\{(.*)\\}".toRegex()
        val togglesRegex = " \\(([\\d,]+)\\)".toRegex()

        val (a, b, c) = parseRegex.find(line)!!.destructured
        val toggles = togglesRegex.findAll(b).map {
            Toggle(it.groups[1]!!.value.split(",").map { it.toInt() })
        }.toSet()
        val joltage = c.split(",").map { it.toInt() }
        return TheInput(a, toggles, joltage)
    }

    override fun solve1(data: List<TheInput>): Long {
        return data.sumOf { togglesTillDesiredState(it) }
    }

    private fun togglesTillDesiredState(input: TheInput): Long {
        val desiredState = input.lights
        val toggles = input.toggles
        var statesAfterNRounds = setOf(".".repeat(desiredState.length))
        var n = 0L

        while (!statesAfterNRounds.contains(desiredState)) {
            n++
            statesAfterNRounds = statesAfterNRounds.flatMap { state ->
                toggles.map { it.applyTo(state) }
            }.toSet()
        }

        return n
    }

    override fun solve2(data: List<TheInput>): Long {
        var sum = 0L
        data.forEachIndexed { i, input ->
            sum += togglesTillDesiredJoltage(input)
            println(i)
        }
        return sum
    }

    private fun togglesTillDesiredJoltage(input: TheInput): Long {
        return -1L
    }

}

data class Toggle(val indices: List<Int>) {
    val opposite = mapOf(
        '#' to '.',
        '.' to '#'
    )

    fun applyTo(lights: String): String {
        val chars = lights.toMutableList()
        indices.forEach {
            chars[it] = opposite[chars[it]]!!
        }
        return chars.joinToString("")
    }

    fun applyTo(joltage: List<Int>): List<Int> {
        val result = joltage.toMutableList()
        indices.forEach {
            result[it]++
        }
        return result.toList()
    }
}

data class TheInput(val lights: String, val toggles: Set<Toggle>, val joltage: List<Int>)
