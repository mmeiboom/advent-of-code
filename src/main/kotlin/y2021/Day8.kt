package y2021

import lib.Day
import lib.resourceLines

object Day8 : Day {

    private val input = resourceLines(2021, 8)

    override fun part1(): Int {
        val regex = Regex("([a-z]+)")
        return input.flatMap {
            val output = it.split("|")[1]
            val matches = regex.findAll(output)
            matches.map { m -> m.groupValues[1] }
        }.count {
            listOf(2, 3, 4, 7).contains(it.length)
        }
    }

    override fun part2(): Int {
        val regex = Regex("([a-z]+)")
        val signals = input.map {
            val matches = regex.findAll(it)
            matches.map { m -> m.groupValues[1].toCharArray().sorted().joinToString("") }
        }
        return signals.sumOf { decode(it.toList()) }
    }

    private fun decode(pairs: List<String>): Int {
        val outputPairs = pairs.takeLast(4)
        val legend = mutableMapOf(
            1 to (pairs.firstOrNull { it.length == 2 } ?: ""),
            4 to (pairs.firstOrNull { it.length == 4 } ?: ""),
            7 to (pairs.firstOrNull { it.length == 3 } ?: ""),
            8 to (pairs.firstOrNull { it.length == 7 } ?: "")
        )

        legend[9] = pairs.first {
            it.length == 6
                    && it.toSet().containsAll(legend.getValue(4).toSet())
                    && it.toSet().containsAll(legend.getValue(1).toSet())
        }

        legend[0] = pairs.first {
            it.length == 6
                    && !it.toSet().containsAll(legend.getValue(4).toSet())
                    && it.toSet().containsAll(legend.getValue(1).toSet())
        }

        legend[6] = pairs.first {
            it.length == 6
                    && !it.toSet().containsAll(legend.getValue(4).toSet())
                    && !it.toSet().containsAll(legend.getValue(1).toSet())
        }

        legend[2] = pairs.first {
            it.length == 5
                    && !legend.getValue(6).toSet().containsAll(it.toSet())
                    && !it.toSet().containsAll(legend.getValue(1).toSet())
        }

        legend[3] = pairs.first {
            it.length == 5
                    && it.toSet().containsAll(legend.getValue(1).toSet())
        }

        legend[5] = pairs.first {
            it.length == 5
                    && legend.getValue(6).toSet().containsAll(it.toSet())
        }


        return outputPairs.map {
            legend.entries.first { (k, v) -> v == it }.key.digitToChar()
        }.joinToString(separator = "").toInt()
    }
}

fun main() {
    println(Day8.part1())
    println(Day8.part2())
}
