package y2020

import lib.Day
import lib.Point
import lib.resourceLines
import y2020.Day11.State.EMPTY
import y2020.Day11.State.OCCUPIED
import kotlin.math.absoluteValue

object Day11 : Day {

    private val seatingArea = seatingArea(resourceLines(2020, 11))

    override fun part1(): Long {
        val neighbours = seatingArea.keys.map { it to it.neighbors().toSet() }.toMap()
        val threshold = 4
        val result = runWhileNotStable(neighbours, threshold)
        return result.count { it.value == OCCUPIED }.toLong()
    }

    override fun part2(): Long {
        val neighbours = seatingArea.keys.map { it to visibleNeighbors(it) }.toMap()
        val threshold = 5
        val result = runWhileNotStable(neighbours, threshold)
        return result.count { it.value == OCCUPIED }.toLong()
    }

    private fun visibleNeighbors(point: Point): Set<Point> {
        val dirs = point.neighbors().map { point.distance(it) }
        val visible = mutableSetOf<Point>()
        dirs.forEach {
            var done = false
            var candidate = point
            while (!done) {
                candidate = candidate.plus(it)
                if (seatingArea.containsKey(candidate)) {
                    visible.add(candidate)
                    done = true
                } else if (candidate.x.absoluteValue > 100 || candidate.y.absoluteValue > 100) {
                    done = true
                }
            }
        }

        return visible
    }

    private fun runWhileNotStable(neighbours: Map<Point, Set<Point>>, threshold: Int): Map<Point, State> {
        var currentArea = seatingArea
        var stable = false
        var i = 0
        // STOP CONDITION FAILING!!
        while (!stable && i < 1000) {
            val updated = update(currentArea, neighbours, threshold)
            stable = stable(updated, currentArea)
            currentArea = updated
            i++
        }
        return currentArea
    }

    private fun stable(a1: Map<Point, State>, a2: Map<Point, State>): Boolean {
        val left = a1.filter { it.value == OCCUPIED }.keys
        val right = a2.filter { it.value == OCCUPIED }.keys
        return left == right
    }

    private fun update(area: Map<Point, State>, neighbours: Map<Point, Set<Point>>, threshhold: Int): Map<Point, State> {
        return area.map {
            val (point, state) = it
            val occupiedNeighbours = neighbours.getValue(point).count { n -> area[n] == OCCUPIED }
            if (state == EMPTY && occupiedNeighbours == 0) {
                point to OCCUPIED
            } else if (state == OCCUPIED && occupiedNeighbours >= threshhold) {
                point to EMPTY
            } else {
                point to state
            }
        }.toMap()
    }

    private fun seatingArea(mapLines: List<String>): Map<Point, State> {
        val area: MutableMap<Point, State> = mutableMapOf()
        mapLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == 'L') {
                    area.put(Point(x, y), EMPTY)
                }
            }
        }
        return area
    }

    enum class State { EMPTY, OCCUPIED }
}


fun main() {
    println(Day11.part1())
    println(Day11.part2())
}