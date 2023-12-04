package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import java.util.*

object Y2022D05 : Day {

    private val input = resourceLines(2022, 5)
    private val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()

    override fun part1(): Any {
        val (area, moves) = readInput()
        area.applyMoves(moves, CrateMover9000())
        return area.takeTopValues()
    }

    override fun part2(): Any {
        val (area, moves) = readInput()
        area.applyMoves(moves, CrateMover9001())
        return area.takeTopValues()
    }

    interface Mover {
        fun apply(move: Move, area: CargoArea)
    }

    class CrateMover9000 : Mover {
        override fun apply(move: Move, area: CargoArea) {
            val crates = area.take(move.from, move.amount)
            area.drop(move.to, crates)
        }
    }

    class CrateMover9001 : Mover {
        override fun apply(move: Move, area: CargoArea) {
            val crates = area.take(move.from, move.amount).reversed()
            area.drop(move.to, crates)
        }
    }

    data class CargoArea(
        val stacks: List<ArrayDeque<Char>>
    ) {
        fun take(from: Int, amount: Int): List<Char> {
            return IntRange(0, amount - 1).map {
                stacks[from - 1].pop()
            }
        }

        fun drop(to: Int, crates: List<Char>) = crates.forEach { stacks[to - 1].push(it) }
        fun applyMoves(moves: List<Move>, mover: Mover) = moves.forEach { mover.apply(it, this) }
        fun takeTopValues() = stacks.map { it.pop() }.joinToString(separator = "")
    }

    data class Move(val amount: Int, val from: Int, val to: Int)

    private fun readInput(): Pair<CargoArea, List<Move>> {
        val split = input.indexOf("")
        val initialState = input.subList(0, split)
        val moves = input.drop(split + 1)
            .map {
                val (_, amount, from, to) =
                    regex.matchEntire(it)!!.groupValues
                Move(amount.toInt(), from.toInt(), to.toInt())
            }

        val nrOfStacks = (initialState.last().length + 1) / 4
        val stacks = mutableListOf<ArrayDeque<Char>>()
        for (i in 0 until nrOfStacks) {
            stacks.add(i, ArrayDeque<Char>())
        }
        initialState
            .reversed()
            .drop(1)
            .forEach { row ->
                (0 until nrOfStacks).forEach { i ->
                    val index = 1 + 4 * i
                    if (row.length >= index && row[index].code >= 'A'.code && row[index].code <= 'Z'.code) {
                        stacks[i].push(row[index])
                    }
                }
            }

        return CargoArea(stacks) to moves
    }

}