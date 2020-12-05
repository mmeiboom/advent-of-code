package y2020

import lib.Day
import lib.resourceLines

object Day5 : Day {

    private val input = resourceLines(2020, 5)

    override fun part1() = input.map { seatId(it) }.maxOf { it }

    override fun part2() =input.map { seatId(it) }
                .sorted()
                .zipWithNext()
                .first { (seat, next) -> next - seat == 2L }
                .let { it.first + 1 }

    private fun seatId(s: String) = s
            .replace('F', '0')
            .replace('B', '1')
            .replace('L', '0')
            .replace('R', '1')
            .toLong(2)
}

fun main() {
    println(Day5.part1())
    println(Day5.part2())
}