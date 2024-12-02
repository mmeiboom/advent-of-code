package nl.mmeiboom.adventofcode.y2024

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.extractNumbers
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2024D02 : Day {

    val input = resourceLines(2024, 2).parse()

    override fun part1() = input.count { it.isSafe() }

    override fun part2() = input.count { it.isSafe(applyDampening = true) }

    private fun List<String>.parse() =
        this.map { extractReport(it) }

    private fun extractReport(it: String): Report {
        return Report(Levels(it.extractNumbers()))
    }

    data class Report(val levels: Levels) {
        fun isSafe(applyDampening: Boolean = false): Boolean {
            val permutations = if (applyDampening) levels.dampen() else listOf(levels)
            return permutations.any { it.graduallyIncrements() || it.graduallyDecrements() }
        }
    }

    data class Levels(val values: List<Long>) {
        fun graduallyIncrements(): Boolean {
            return values.zipWithNext().all { (a, b) -> b - a in 1..3 }
        }

        fun graduallyDecrements(): Boolean {
            return values.zipWithNext().all { (a, b) -> a - b in 1..3 }
        }

        fun dampen(): List<Levels> {
            val allDampenedLevels = mutableListOf(this)
            values.indices.forEach {
                val dampenedValues = values.toMutableList()
                dampenedValues.removeAt(it)
                allDampenedLevels.add(Levels(dampenedValues))
            }
            return allDampenedLevels
        }
    }

}

fun main() {
    println(Y2024D02.part1())
    println(Y2024D02.part2())
}