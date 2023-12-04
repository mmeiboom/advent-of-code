package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2022D03 : Day {

    val input = resourceLines(2022, 3)

    override fun part1(): Any {
        return input
            .map { toOverlappingType(it) }
            .sumOf { toValue(it) }
    }

    private fun toValue(c: Char): Int {
        val ascii = c.toInt()
        if (ascii < 97) { // A-Z
            return ascii - 38
        }
        return ascii - 96
    }

    private fun toOverlappingType(line: String): Char {
        val left = line.substring(0, line.length / 2).toSet()
        val right = line.substring(line.length / 2).toSet()
        return left.intersect(right).single()
    }

    private fun toOverlappingTypes(first: String, second: String, third: String): Char {
        return (first.toSet()).intersect(second.toSet()).intersect(third.toSet()).single()
    }

    override fun part2(): Any {
        var i = 0
        var sum = 0
        while(i < input.size) {
            sum += toValue(toOverlappingTypes(input[i], input[i+1], input[i+2]))
            i += 3
        }
        return sum
    }
}