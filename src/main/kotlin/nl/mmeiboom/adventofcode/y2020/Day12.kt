package nl.mmeiboom.adventofcode.y2020

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Direction
import nl.mmeiboom.adventofcode.lib.Point
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day12 : Day {

    private val instructions = resourceLines(2020, 12).map { Instruction(it[0], it.substring(1).toInt()) }

    override fun part1() : Long {
        val start = Point(0,0)
        var position = start
        var direction = Direction.EAST
        instructions.forEach {
            when(it.instruction) {
                'N' -> position = position.up(it.distance)
                'E' -> position = position.right(it.distance)
                'S' -> position = position.down(it.distance)
                'W' -> position = position.left(it.distance)
                'F' -> repeat(it.distance) { position = position.plus(direction) }
                'L' -> repeat(it.distance / 90) { direction = direction.ccw() }
                'R' -> repeat(it.distance / 90) { direction = direction.cw() }
            }
        }

        return start.manhattan(position).toLong()
    }

    override fun part2() : Long {
        var start = Point(0,0)
        var ship = Point(0,0)
        var waypoint = Point(10, -1)
        instructions.forEach {
            when(it.instruction) {
                'N' -> waypoint = waypoint.up(it.distance)
                'E' -> waypoint = waypoint.right(it.distance)
                'S' -> waypoint = waypoint.down(it.distance)
                'W' -> waypoint = waypoint.left(it.distance)
                'F' -> repeat(it.distance) { ship = ship.plus(waypoint) }
                'L' -> repeat(4 - it.distance / 90) { waypoint = waypoint.rotate90() }
                'R' -> repeat(it.distance / 90) { waypoint = waypoint.rotate90() }
            }
        }

        return start.manhattan(ship).toLong()
    }

    data class Instruction(val instruction: Char, val distance: Int)
}

fun main() {
    println(Day12.part1())
    println(Day12.part2())
}