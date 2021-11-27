import lib.Day
import lib.Direction
import lib.Point
import lib.resourceLines
import java.util.*

object Day3 : Day {

    private val wires = resourceLines(2019, 3).map { wire -> wire.split(",").map { Direction.from(it[0]) to it.substring(1).toInt() } }
    private val pointsPerWire = wires.map { toPoints(it) }
    private val intersections = pointsPerWire.map { points -> points.map { it.first } }.let { (first, second) -> first.intersect(second) }

    private fun toPoints(list: List<Pair<Direction, Int>>): List<Pair<Point, Int>> {
        var current = Point(0, 0)
        val points = mutableListOf<Pair<Point, Int>>()
        var distance = 0

        list.forEach { (dir, steps) ->
            repeat(steps) {
                current += dir
                distance++
                points += current to distance
            }
        }

        return points
    }

    override fun part1() = intersections.map { it.manhattan() }.minOrNull()!!

    override fun part2() : Any {
        val (first, second) = pointsPerWire.map { list -> list.filter { intersections.contains(it.first) }.toMap() }
        return first.map { it.value + second.getValue(it.key) }.minOrNull()!!
    }
}
