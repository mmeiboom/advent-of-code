import lib.Day
import lib.Point
import lib.resourceLines
import kotlin.math.min

object Day18 : Day {

    private val map = toPoints(resourceLines(2019, 18))

    override fun part1(): Any {
        val initialPosition = map.filterValues { it == '@' }.keys.first()
        val keysToCollect = map.values.filter { CharRange('a', 'z').contains(it) }.toList()

        return distanceToCollectKeys('@', keysToCollect, initialPosition)
    }

    private fun distanceToCollectKeys(currentKey: Char, keys: List<Char>, currentPosition: Point): Int {
        if (keys.isEmpty()) return 0
        val cacheKey = currentKey + keys.joinToString("")
        if (cache.containsKey(cacheKey)) {
            return cache.getValue(cacheKey)
        }

        val possibleNextKeys = scanForKeys(currentPosition, keys)
        var shortest = Int.MAX_VALUE

        possibleNextKeys.forEach { key ->
            val totalDistance = key.distance + distanceToCollectKeys(key.key, keys - key.key, key.point)
            shortest = min(shortest, totalDistance)
        }

        cache[cacheKey] = shortest
        return shortest
    }

    private val cache: MutableMap<String, Int> = mutableMapOf()

    private fun scanForKeys(initialPosition: Point, keysToCollect: List<Char>): List<ReachableKey> {
        var currentPosition = initialPosition
        val discoveredArea: MutableMap<Point, Int> = mutableMapOf(Pair(initialPosition, 0))
        val previousPositions = mutableListOf<Point>()
        val reachableKeys = mutableListOf<ReachableKey>()

        while (true) {
            var backtracking = false
            var nextPosition = currentPosition.neighborsHv().find { !discoveredArea.containsKey(it) }
            if (nextPosition == null) {
                backtracking = true
                if (previousPositions.isEmpty()) {
                    return reachableKeys
                }
                nextPosition = previousPositions.removeAt(previousPositions.size - 1)
            }

            val nextChar = map.getValue(nextPosition)

            if (nextChar == '#') {
                discoveredArea[nextPosition] = -1
            } else if (nextChar == '.'
                    || nextChar == '@'
                    || !keysToCollect.contains(nextChar.toLowerCase())
            ) {
                if (!backtracking) {
                    previousPositions.add(currentPosition)
                    discoveredArea[nextPosition] = min(discoveredArea.getOrDefault(nextPosition, Int.MAX_VALUE), discoveredArea.getValue(currentPosition) + 1)
                }
                currentPosition = nextPosition
            } else if (nextChar in 'a'..'z') {
                discoveredArea[nextPosition] = min(discoveredArea.getOrDefault(nextPosition, Int.MAX_VALUE), discoveredArea.getValue(currentPosition) + 1)
                reachableKeys.add(ReachableKey(nextPosition, map.getValue(nextPosition), discoveredArea.getValue(nextPosition)))
            } else if (nextChar in 'A'..'Z') {
                discoveredArea[nextPosition] = -1
            }
        }
    }

    data class ReachableKey(val point: Point, val key: Char, val distance: Int)

    override fun part2(): Any {
        return "nyi" // day18b(resourceLines(2019, 18))
    }

    private fun toPoints(mapLines: List<String>): Map<Point, Char> {
        val points = mutableMapOf<Point, Char>()
        mapLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                points[Point(x, y)] = char
            }
        }
        return points
    }

}

fun main() {
    println(Day18.part1())
    println(Day18.part2())
}