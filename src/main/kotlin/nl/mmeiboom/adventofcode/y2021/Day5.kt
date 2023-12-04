package nl.mmeiboom.adventofcode.y2021

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Line
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day5 : Day {

    private val input = resourceLines(2021, 5).map { toLine(it) }

    override fun part1(): Int {
        val counts = input.filter {it.isVertical() || it.isHorizontal()}
            .flatMap { it.pointsOnLine() }
            .groupBy { it }
            .mapValues { it.value.count() }

        return counts.count { it.value > 1 }
    }

    override fun part2(): Int {
        val counts = input
            .flatMap { it.pointsOnLine() }
            .groupBy { it }
            .mapValues { it.value.count() }

        return counts.count { it.value > 1 }
    }

    private fun toLine(line: String): Line {
        val match = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)").find(line)!!
        val (x1, y1, x2, y2) = match.destructured
        return Line(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
    }
}

fun main() {
    println(Day5.part1())
    println(Day5.part2())
}
