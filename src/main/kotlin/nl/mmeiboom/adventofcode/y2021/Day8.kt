package nl.mmeiboom.adventofcode.y2021

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.lib.sorted

object Day8 : Day {

    private val input = resourceLines(2021, 8)

    override fun part1(): Int {
        val displays = parseInput()
        return displays
            .flatMap { it.takeLast(4) }
            .count { listOf(2, 3, 4, 7).contains(it.length) }
    }

    override fun part2(): Int {
        val displays = parseInput()
        return displays.sumOf {
            val signal = it.take(10)
            val output = it.takeLast(4)
            decode(mapWires(signal), output)
        }
    }

    private fun parseInput(): List<List<String>> {
        val regex = Regex("([a-z]+)")
        val displays = input.map {
            val matches = regex.findAll(it)
            matches.map { m -> m.groupValues[1].sorted() }.toList()
        }
        return displays
    }

    private fun mapWires(signal: List<String>): Map<String, Int> {
        class Digit {
            val candidates = signal.toMutableList()

            fun segments(count: Int) {
                candidates.removeIf { it.length != count }
            }

            fun matches(digit: Digit, overlap: Int) {
                candidates.removeIf { candidate ->
                    candidate.count { digit.letters.contains(it) } != overlap
                }
            }

            val letters get() = candidates.single()
        }

        fun digit(lambda: Digit.() -> Unit) = Digit().apply(lambda)

        val one = digit { segments(2) }
        val four = digit { segments(4) }
        val seven = digit { segments(3) }
        val eight = digit { segments(7) }

        val zero = digit {
            segments(6)
            matches(four, 3)
            matches(seven, 3)
        }

        val two = digit {
            segments(5)
            matches(one, 1)
            matches(four, 2)
        }
        val three = digit {
            segments(5)
            matches(seven, 3)
            matches(four, 3)
        }
        val five = digit {
            segments(5)
            matches(one, 1)
            matches(four, 3)
        }
        val six = digit {
            segments(6)
            matches(one, 1)
        }
        val nine = digit {
            segments(6)
            matches(four, 4)
            matches(seven, 3)
        }

        return mapOf(
            zero.letters to 0,
            one.letters to 1,
            two.letters to 2,
            three.letters to 3,
            four.letters to 4,
            five.letters to 5,
            six.letters to 6,
            seven.letters to 7,
            eight.letters to 8,
            nine.letters to 9,
        )
    }

    private fun decode(legend: Map<String, Int>, output: List<String>): Int {
        return output.map {
            legend.getValue(it).digitToChar()
        }.joinToString(separator = "").toInt()
    }
}

fun main() {
    println(Day8.part1())
    println(Day8.part2())
}
