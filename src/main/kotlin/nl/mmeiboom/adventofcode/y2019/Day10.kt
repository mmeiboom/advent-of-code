import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.gcd
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.abs
import kotlin.math.atan2

object Day10 : Day {

    private val map = toPoints(resourceLines(2019, 10))
    private val station = Point2D(x = 11, y = 19)

    override fun part1() = map.map { observables(it) }.maxOrNull() ?: 0

    private fun observables(point: Point2D): Int {
        return map.filter { p -> canObserve(point, p) }.size
    }

    private fun canObserve(observer: Point2D, observable: Point2D): Boolean {
        if (observer == observable) {
            return false
        }
        return asteroidsInBetween(observer, observable) == 0
    }

    private fun asteroidsInBetween(observer: Point2D, observable: Point2D): Int {
        val distance = observable.distance(observer)
        val gcd = gcd(abs(distance.x), abs(distance.y))

        val stepX = distance.x / gcd
        val stepY = distance.y / gcd
        val steps = if (stepX != 0) distance.x / stepX else distance.y / stepY
        var points = 0
        for (i in 1 until steps) {
            val pointInBetween = Point2D(stepX * i, stepY * i).plus(observer)
            if (map.contains(pointInBetween)) {
                points++
            }
        }

        return points
    }

    override fun part2(): Any {
        val mapWithoutStation = map.toMutableList()
        mapWithoutStation.remove(station)

        val candidates = mapWithoutStation.filter { asteroidsInBetween(it, station) == 0 }
            .sortedWith(Comparator { p1, p2 -> compare(p1, p2) })
        val asteroid200 = candidates[199]
        return asteroid200.x * 100 + asteroid200.y
    }

    private fun compare(p1: Point2D, p2: Point2D): Int {
        val v1 = Point2D(p1.x - station.x, station.y - p1.y)
        val v2 = Point2D(p2.x - station.x, station.y - p2.y)
        val q1 = quadrant(v1)
        val q2 = quadrant(v2)
        if (q1 != q2) {
            return q1.compareTo(q2)
        }

        return angle(v1).compareTo(angle(v2))
    }

    private fun angle(v1: Point2D): Double {
        return atan2(v1.x.toDouble(), v1.y.toDouble())
    }

    private fun quadrant(it: Point2D): Int {
        if (it.x >= 0) {
            if (it.y >= 0) {
                return 1
            }
            return 2
        }
        if (it.y <= 0) {
            return 3
        }
        return 4
    }

    private fun toPoints(mapLines: List<String>): List<Point2D> {
        val points: MutableList<Point2D> = mutableListOf()
        mapLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == '#') {
                    points.add(Point2D(x, y))
                }
            }
        }
        return points
    }
}

fun main() {
    println(Day10.part1())
    println(Day10.part2())
}
