package nl.mmeiboom.adventofcode.y2023

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2023D08 : Day {

    val input = resourceLines(2023, 8)
    val instructions = input.first().map { it }
    val network = input.parse()

    override fun part1(): Int {
        var location = "AAA"
        var i = 0
        while (location != "ZZZ") {
            val direction = instructions[i % instructions.size]
            when (direction) {
                'L' -> location = network.getValue(location).first
                'R' -> location = network.getValue(location).second
            }
            i++
        }

        return i
    }

    override fun part2() = -1

    private fun List<String>.parse(): Map<String, Pair<String, String>> {
//        val instructions = this.first().map { it }
        val network = mutableMapOf<String, Pair<String, String>>()
        this.drop(2).forEach { line ->
            val from = line.substring(0, 3)
            val left = line.substring(7, 10)
            val right = line.substring(12, 15)
            network[from] = Pair(left, right)
        }

        return network.toMap()
    }
}

fun main() {
    print(Y2023D08.part1())
}