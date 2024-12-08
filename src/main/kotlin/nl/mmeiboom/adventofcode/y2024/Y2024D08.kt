package nl.mmeiboom.adventofcode.y2024

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.lib.toPointsMap

object Y2024D08 : Day {

    val input = resourceLines(2024, 8).toPointsMap()

    override fun part1() : Long {
        val antinodes = mutableSetOf<Point2D>()
        val pointsPerAntenna = input.entries
            .groupBy { it.value }
            .filter { (k, _) -> k.isDigit() || k.isLetter()}
            .map { (k, v) -> k to v.map { it.key }}
            .associate { (k, v) -> k to v }

        pointsPerAntenna.forEach { (k, v) ->
            for(a1 in 0..v.size -1) {
                for(a2 in (a1 + 1)..v.size - 1) {
                    val an1 = v[a1] + (v[a1] - v[a2])
                    val an2 = v[a2] + (v[a2] - v[a1])
                    antinodes.add(an1)
                    antinodes.add(an2)
                }
            }
        }

        return antinodes.count { it in input.keys }.toLong()
    }

    override fun part2() : Long {
        val antinodes = mutableSetOf<Point2D>()
        val pointsPerAntenna = input.entries
            .groupBy { it.value }
            .filter { (k, _) -> k.isDigit() || k.isLetter() }
            .map { (k, v) -> k to v.map { it.key } }
            .associate { (k, v) -> k to v }

        pointsPerAntenna.forEach { (k, v) ->
            for (a1 in 0..v.size - 1) {
                for (a2 in (a1 + 1)..v.size - 1) {
                    val dist = (v[a1] - v[a2])
                    var back = v[a1]
                    while (back in input.keys) {
                        antinodes.add(back)
                        back -= dist
                    }
                    var forward = v[a1]
                    while (forward in input.keys) {
                        antinodes.add(forward)
                        forward += dist
                    }
                }
            }
        }

        return antinodes.size.toLong()
    }
}

fun main() {
    println(Y2024D08.part1())
    println(Y2024D08.part2())
}