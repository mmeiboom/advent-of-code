package nl.mmeiboom.adventofcode.y2025

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2025D01 : Day {

    val input = resourceLines(2025, 1).parse()

    override fun part1(): Long {
        var dial = 50L
        var count = 0L
        input.forEach { (dir, steps) ->
            val offset = steps % 100
            dial = if(dir == 'L') {
                (dial - offset) % 100
            } else {
                (dial + offset) % 100
            }

            if(dial == 0L) count++
        }

        return count
    }

    override fun part2(): Long {
        val disc = 100L
        var dial = 50L
        var count = 0L
        input.forEach { (dir, steps) ->
            count += steps.div(disc) // full rotations, count and  drop
            val offset = steps % disc // remainder
            if((dial in 1 until offset && dir == 'L') || (dial > 0 && offset + dial > disc && dir == 'R')) {
                count++
            }

            dial = if(dir == 'L') {
                (dial - offset + 100) % 100
            } else {
                (dial + offset) % 100
            }

            if(dial == 0L) count++
        }

        return count
    }

    private fun List<String>.parse() =
        this.map { extractNumberPair(it) }

    private fun extractNumberPair(it: String): Pair<Char, Long> {
        return Pair(
            it.first(),
            it.slice(1..it.lastIndex).toLong()
        )
    }
}

fun main() {
    println(Y2025D01.part1())
    println(Y2025D01.part2())
}