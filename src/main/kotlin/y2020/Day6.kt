package y2020

import lib.Day
import lib.resourceLines
import lib.splitByDoubleNewLine

object Day6 : Day {

    private val input = resourceLines(2020, 6)

    override fun part1() = input.splitByDoubleNewLine()
            .map { it.flatMap { l -> l.asIterable() } }
            .map { it.toSet().size }
            .sum()

    override fun part2() = input.splitByDoubleNewLine()
            .map { countOverlappingCharacters(it) }
            .sum()

    private fun countOverlappingCharacters(strings: List<String>) =
            strings.map { l -> l.asIterable().toSet() }
                    .reduce { acc, set -> acc.intersect(set) }
                    .size
}

fun main() {
    println(Day6.part1())
    println(Day6.part2())
}