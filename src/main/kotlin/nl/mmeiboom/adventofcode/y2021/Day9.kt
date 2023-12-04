package nl.mmeiboom.adventofcode.y2021

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day9 : Day {

    private val input = oceanFloor(resourceLines(2021, 9))

    override fun part1(): Int {
        return input
            .filter {
                it.key.neighborsHv().all { n -> input.getOrDefault(n, 9) > it.value }
            }
            .map { it.value + 1 }
            .sum()
    }

    override fun part2(): Int {
        return input
            .filter {
                it.key.neighborsHv().all { n -> input.getOrDefault(n, 9) > it.value }
            }
            .map { basin(it.key) }
            .sortedBy { it }
            .takeLast(3)
            .reduce { acc, i -> acc * i }
    }

    private fun basin(startingPoint: Point): Int {
        val basin = mutableSetOf<Point>()
        val pointsToCheck = ArrayDeque<Point>()
        pointsToCheck.add(startingPoint)
        while(pointsToCheck.isNotEmpty()) {
            val p = pointsToCheck.removeFirst()
            if(!basin.contains(p) && input.getOrDefault(p, 9) < 9) {
                basin.add(p)
                pointsToCheck.addAll(p.neighborsHv())
            }
        }
        return basin.size
    }

    private fun oceanFloor(mapLines: List<String>): Map<Point, Int> {
        val area: MutableMap<Point, Int> = mutableMapOf()
        mapLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                area[Point(x, y)] = char - '0'
            }
        }
        return area
    }
}

fun main() {
    println(Day9.part1())
    println(Day9.part2())
}
