package y2020

import lib.Day
import lib.combinations
import lib.resourceLines

object Day1 : Day {

    private val entries = resourceLines(2020, 1).map { it.toLong() }

    override fun part1() = find(2)

    override fun part2() = find(3)

    private fun find(nrOfParts: Int): Long {
        entries.combinations(nrOfParts).forEach {
            if (it.sum() == 2020L) {
                return it.reduce { acc, i -> acc * i }
            }
        }
        return -1
    }

}

fun main() {
    println(Day1.part1())
    println(Day1.part2())
}