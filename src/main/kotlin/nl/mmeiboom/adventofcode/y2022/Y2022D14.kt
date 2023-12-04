package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2022D14 : Day {

    val input = resourceLines(2022, 14)

    override fun part1(): Any {
        val map = mutableMapOf<Point2D, Char>()
        input.forEach { drawOnMap(it, map) }
        return pourSand(map)
    }

    private fun pourSand(map: MutableMap<Point2D, Char>): Int {
        var done = false
        var count = 0
        while (!done) {
            var sandPosition = Point2D(500, 0)
            var atRest = false
            while (!done && !atRest) {
                done = onlyAirBelow(sandPosition, map)
                val options = listOf(
                    sandPosition.down(),
                    sandPosition.down().left(),
                    sandPosition.down().right(),
                )
                val nextPosition = options.firstOrNull { !map.contains(it) }
                nextPosition?.let { sandPosition = it }
                atRest = nextPosition == null
            }

            // Increment only if the grain found a resting place
            if (atRest) {
                map[sandPosition] = 'o'
                count += 1
            }
        }
        return count
    }

    private fun pourSand2(map: MutableMap<Point2D, Char>): Int {
        val floorY = map.keys.maxOf { it.y } + 1
        var done = false
        var count = 0
        while (!done) {
            var sandPosition = Point2D(500, 0)
            var atRest = false
            while (!done && !atRest) {
                val options = listOf(
                    sandPosition.down(),
                    sandPosition.down().left(),
                    sandPosition.down().right(),
                )
                val nextPosition = options.firstOrNull { !map.contains(it) }
                nextPosition?.let { sandPosition = it }
                atRest = nextPosition == null || nextPosition.y == floorY
                done = atRest && sandPosition == Point2D(500, 0)
            }

            // Increment only if the grain found a resting place
            if (atRest) {
                map[sandPosition] = 'o'
                count += 1
            }
        }
        return count
    }

    private fun onlyAirBelow(sandPosition: Point2D, map: Map<Point2D, Char>): Boolean {
        return map.keys.none { it.x == sandPosition.x && it.y > sandPosition.y }
    }

    private fun drawOnMap(line: String, map: MutableMap<Point2D, Char>) {
        val points = "(\\d+,\\d+)".toRegex().findAll(line).toList()
            .map {
                val (x, y) = it.value.split(",")
                Point2D(x.toInt(), y.toInt())
            }
        points.zipWithNext { a, b ->
            val dir = a.directionTo(b)
            var current = a
            while (current != b) {
                map[current] = '#'
                current = current.plus(dir)
            }
            map[current] = '#'
        }
    }

    override fun part2(): Any {
        val map = mutableMapOf<Point2D, Char>()
        input.forEach { drawOnMap(it, map) }
        return pourSand2(map)
    }

    private fun printMap(currentMap: Map<Point2D, Char>, min: Point2D = Point2D(0, 0), max: Point2D = Point2D(10, 510)) {
        for (y in min.y until max.y) {
            for (x in min.x until max.x) {
                print(currentMap.getOrDefault(Point2D(x, y), '.'))
            }
            println()
        }
        println("-------")
    }

}