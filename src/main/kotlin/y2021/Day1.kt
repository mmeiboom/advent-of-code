package y2021

import lib.Day
import lib.resourceLines

object Day1 : Day {

    private val input = resourceLines(2021, 1).map { it.toLong() }

    override fun part1() = input.zipWithNext().count { (a, b) -> a < b }

    override fun part2() = input.windowed(3)
        .zipWithNext()
        .count { (a, b) -> a.sum() < b.sum() }
}

fun main() {
    println(Day1.part1())
    println(Day1.part2())
}
