package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.lib.toPointsMap

object Y2022D12 : Day {

    val map = resourceLines(2022, 12).toPointsMap()

    override fun part1(): Any {
        val start = map.entries.first { it.value == 'S'}.key
        val counts = mutableMapOf(start to 0)
        val remaining = mutableListOf(start)
        while (remaining.isNotEmpty()) {
            val point = remaining.removeFirst()
            point.neighborsHv()
                .filter { map.contains(it) }
                .filter { canTravelFromTo(point, it) }
                .forEach {
                    if (!counts.containsKey(it) || counts.getValue(it) > counts.getValue(point) + 1) {
                        remaining.add(it)
                        counts[it] = counts.getValue(point) + 1
                    }
                }
        }
        val end = map.entries.first { it.value == 'E' }.key
        return counts.getValue(end)
    }

    private fun canTravelFromTo(a: Point2D, b: Point2D): Boolean {
        var c1 = map.getValue(a)
        if (c1 == 'E') c1 = 'z'
        if (c1 == 'S') c1 = 'a'
        var c2 = map.getValue(b)
        if (c2 == 'E') c2 = 'z'
        if (c2 == 'S') c2 = 'a'
        return c1.code - c2.code >= -1
    }

    override fun part2(): Any {
        val starts = map.entries.filter { it.value == 'S' || it.value == 'a'}.map { it.key }
        val counts = mutableMapOf<Point2D, Int>()
        val remaining = mutableListOf<Point2D>()
        starts.forEach {
            remaining.add(it)
            counts[it] = 0
        }
        while (remaining.isNotEmpty()) {
            val point = remaining.removeFirst()
            point.neighborsHv()
                .filter { map.contains(it) }
                .filter { canTravelFromTo(point, it) }
                .forEach {
                    if (!counts.containsKey(it) || counts.getValue(it) > counts.getValue(point) + 1) {
                        remaining.add(it)
                        counts[it] = counts.getValue(point) + 1
                    }
                }
        }
        val end = map.entries.first { it.value == 'E' }.key
        return counts.getValue(end)
    }

    private fun printMap(currentMap: Map<Point2D, Char>) {
        for (y in 0 until 5) {
            for (x in 0 until 5) {
                print(currentMap.getValue(Point2D(x, y)))
            }
            println()
        }
        println("-------")
    }

    private fun printReachableMap(counts: Map<Point2D, Int>) {
        for (y in 0 until 41) {
            for (x in 0 until 143) {
                if (counts.containsKey(Point2D(x, y))) {
                    print(map.getValue(Point2D(x, y)))
                } else {
                    print(' ')
                }
            }
            println()
        }
        println("-------")
    }

}