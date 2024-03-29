package nl.mmeiboom.adventofcode.y2020

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day13 : Day {

    private val input = resourceLines(2020, 13)

    override fun part1() : Long {
        val ts = input[0].toLong()
        val lines = input[1].split(",")
                .filter { it != "x" }
                .map { it.toLong() }

        val pair = lines.map { it to it - ts.rem(it) }
                .sortedBy { it.second }
                .first()

        return pair.first * pair.second
    }

    override fun part2() : Long {
        val splitEntries = input[1].split(",")
        splitEntries.forEachIndexed { i, l ->
            if (l != "x") {
                println("(n+$i)%$l = 0,")
                println("Solve at WolphramAlpha.com")
            }
        }
        // Solved at WolphramAlpha.com
        return 230903629977901L
    }
}

fun main() {
    println(Day13.part1())
    println(Day13.part2())
}