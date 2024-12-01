package nl.mmeiboom.adventofcode.y2024

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.absoluteValue

object Y2024D01 : Day {

    val input = resourceLines(2024, 1).parse()

    override fun part1() =
        input.first
            .zip(input.second)
            .sumOf { (a, b) -> (a - b).absoluteValue }

    override fun part2() =
        input.first.sumOf { value -> value * input.second.count { it == value } }

    private fun List<String>.parse() =
        this.map { extractNumberPair(it) }.unzip()

    private fun extractNumberPair(it: String): Pair<Long, Long> {
        val numbers = it.split("   ")
        return Pair(
            numbers[0].toLong(),
            numbers[1].toLong()
        )
    }
}

fun main() {
    println(Y2024D01.part1())
    println(Y2024D01.part2())
}