package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Direction
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2022D08 : Day {

    val map = toTreeHeights(resourceLines(2022, 8))

    override fun part1(): Any {
        return map.keys.count { isVisible(it) }
    }

    override fun part2(): Any {
        return map.keys
            .filter { isVisible(it) }
            .maxOf { scenicScore(it) }
    }

    private fun scenicScore(point: Point2D): Int {
        return Direction.values()
            .map { viewingDistance(point, it) }
            .reduce { acc, int -> acc * int }
    }

    private fun viewingDistance(
        point: Point2D,
        direction: Direction
    ): Int {
        var count = 0
        var neighbour = point.plus(direction)
        while (map.containsKey(neighbour)) {
            count++
            if (map.getValue(neighbour) >= map.getValue(point)) {
                break
            }
            neighbour = neighbour.plus(direction)
        }
        return count
    }

    private fun isVisible(point: Point2D): Boolean {
        return Direction.values()
            .any { isVisibleFrom(point, it) }
    }

    private fun isVisibleFrom(point: Point2D, direction: Direction): Boolean {
        var neighbour = point.plus(direction)
        while (map.containsKey(neighbour)) {
            if (map.getValue(neighbour) >= map.getValue(point)) {
                return false
            }
            neighbour = neighbour.plus(direction)
        }
        return true
    }

    private fun toTreeHeights(mapLines: List<String>): Map<Point2D, Int> {
        val map: MutableMap<Point2D, Int> = mutableMapOf()
        mapLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                map[Point2D(x, y)] = char.toString().toInt()
            }
        }
        return map
    }

}