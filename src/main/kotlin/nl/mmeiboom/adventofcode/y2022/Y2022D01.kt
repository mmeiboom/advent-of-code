package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2022D01 : Day {

    val input = resourceLines(2022, 1)

    override fun part1(): Any {
        val totals = groupCalories()
        return totals.max()
    }

    override fun part2(): Any {
        val totals = groupCalories()
        val sorted = totals.sortedDescending()
        return sorted.take(3).sum()
    }

    private fun groupCalories(): List<Int> {
        var current = 0
        val totals = mutableListOf<Int>()
        input.forEach {
            if (it.isBlank()) {
                totals.add(current)
                current = 0
            } else {
                current += it.toInt()
            }
        }
        return totals
    }
}