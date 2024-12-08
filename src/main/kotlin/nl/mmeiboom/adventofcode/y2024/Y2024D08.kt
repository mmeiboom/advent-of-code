package nl.mmeiboom.adventofcode.y2024

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.combinations
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.lib.toPointsMap

object Y2024D08 : Day {

    private val input = resourceLines(2024, 8).toPointsMap()
    private val pointsPerAntenna = input.entries
        .groupBy { it.value }
        .filter { (k, _) -> k.isDigit() || k.isLetter() }
        .map { (k, v) -> k to v.map { it.key } }
        .associate { (k, v) -> k to v }

    override fun part1(): Long {
        val antiNodes = mutableSetOf<Point2D>()

        pointsPerAntenna.forEach { (_, points) ->
            points.combinations(2).forEach { (a1, a2) ->
                antiNodes.add(a1 + (a1 - a2))
                antiNodes.add(a2 + (a2 - a1))
            }
        }

        return antiNodes.count { it in input.keys }.toLong()
    }

    override fun part2(): Long {
        val antiNodes = mutableSetOf<Point2D>()
        pointsPerAntenna.forEach { (_, points) ->
            points.combinations(2).forEach { (a1, a2) ->
                antiNodes.addAll(findAntiNodes(a1, a1 - a2))
                antiNodes.addAll(findAntiNodes(a1, a2 - a1))
            }
        }

        return antiNodes.size.toLong()
    }

    private fun findAntiNodes(start: Point2D, dist: Point2D): Collection<Point2D> {
        val antiNodes = mutableSetOf<Point2D>()
        var current = start
        while (current in input.keys) {
            antiNodes.add(current)
            current += dist
        }
        return antiNodes
    }
}

fun main() {
    println(Y2024D08.part1())
    println(Y2024D08.part2())
}