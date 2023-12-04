package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.absoluteValue

object Y2022D20 : Day {

    val input = resourceLines(2022, 20).map { it.toInt() }

    override fun part1() = decryptCoordinates(1L, 1).sum()

    override fun part2() = decryptCoordinates(811589153L, 10).sum()

    private fun decryptCoordinates(decryptionKey: Long, rounds: Int): List<Long> {
        val instructions = input.indices.associateWith { input[it] * decryptionKey }.toMap()
        val state = input.indices.associateWith { input[it] * decryptionKey }.toList().toMutableList()

        repeat(rounds) {
            mix(instructions, state)
        }

        return extractCoordinates(state)
    }

    private fun extractCoordinates(state: MutableList<Pair<Int, Long>>): List<Long> {
        val result = state.map { it.second }
        val zeroIndex = result.indexOf(0)
        val thousand = result[(zeroIndex + 1000) % result.size]
        val twoThousand = result[(zeroIndex + 2000) % result.size]
        val threeThousand = result[(zeroIndex + 3000) % result.size]
        return listOf(thousand, twoThousand, threeThousand)
    }

    private fun mix(instructions: Map<Int, Long>, state: MutableList<Pair<Int, Long>>) {
        instructions.forEach { (idx, shift) ->
            val pair = Pair(idx, shift)
            val currentPosition = state.indexOf(pair)
            val normalizedShift = shift % (state.size - 1)
            var newPosition = currentPosition + normalizedShift
            if (newPosition < 0) {
                val wrapAdjustment = newPosition.absoluteValue / state.size + 1
                newPosition = (newPosition + (state.size * wrapAdjustment) - wrapAdjustment) % state.size
            } else if (newPosition >= state.size) {
                val wrapAdjustment = newPosition / state.size
                newPosition = (newPosition + wrapAdjustment) % state.size
            }
            state.remove(pair)
            state.add(newPosition.toInt(), pair)
        }
    }

}

fun main() {
    val p1 = Y2022D20.part1()
    println("P1: $p1")
    val p2 = Y2022D20.part2()
    println("P2: $p2")

}