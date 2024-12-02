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
        return Report(it.extractNumbers())
    }

    data class Report(val levels: List<Long>) {
        fun isSafe(applyDampening: Boolean = false): Boolean {
            if(!applyDampening) return levelsGraduallyIncrement(levels) || levelsGraduallyDecrement(levels)
            val dampenedLevels = dampen(levels)
            return dampenedLevels.any { levelsGraduallyIncrement(it) || levelsGraduallyDecrement(it) }
        }

        private fun dampen(levels: List<Long>): List<List<Long>> {
            val allDampenedLevels = mutableListOf(levels)
            levels.indices.forEach {
                val dampenedLevels = levels.toMutableList()
                dampenedLevels.removeAt(it)
                allDampenedLevels.add(dampenedLevels)
            }
            return allDampenedLevels
        }

        private fun levelsGraduallyIncrement(levels: List<Long>): Boolean {
            return levels.zipWithNext().all { (a,b) -> b - a in 1..3 }
        }

        private fun levelsGraduallyDecrement(levels: List<Long>): Boolean {
            return levels.zipWithNext().all { (a,b) -> a - b in 1..3 }
        }
    }
}

fun main() {
    println(Y2024D02.part1())
    println(Y2024D02.part2())
}