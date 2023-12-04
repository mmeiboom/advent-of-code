package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Direction
import nl.mmeiboom.adventofcode.lib.Direction.*
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2022D09 : Day {

    val input = resourceLines(2022, 9).parse()

    override fun part1(): Any {
        val start = Point2D(0, 0)
        val rope = mutableListOf(start, start)
        return simulateMotion(rope)
    }

    override fun part2(): Any {
        val start = Point2D(0, 0)
        val rope = mutableListOf(start, start, start, start, start, start, start, start, start, start)
        return simulateMotion(rope)
    }

    private fun simulateMotion(rope: MutableList<Point2D>): Int {
        val visited = mutableSetOf(rope.first())

        input.forEach { motion ->
            repeat(motion.distance) {
                // Move head
                rope[0] = rope[0].plus(motion.direction)

                // Update rest
                for (i in 1 until rope.size) {
                    rope[i] = updatePair(rope[i - 1], rope[i])
                }

                visited.add(rope.last())
            }
        }

        return visited.size
    }

    private fun updatePair(head: Point2D, tail: Point2D): Point2D {
        if (head == tail || head.neighbors().contains(tail)) {
            return tail
        }

        // Not adjacent, update
        val newX = when {
            head.x > tail.x -> tail.x + 1
            head.x < tail.x -> tail.x - 1
            else -> tail.x
        }
        val newY = when {
            head.y > tail.y -> tail.y + 1
            head.y < tail.y -> tail.y - 1
            else -> tail.y
        }
        return Point2D(newX, newY)
    }

    data class Motion(val direction: Direction, val distance: Int)

    private fun List<String>.parse(): List<Motion> {
        return this.map {
            val dir = when (it[0]) {
                'R' -> EAST
                'U' -> NORTH
                'L' -> WEST
                'D' -> SOUTH
                else -> throw Error()
            }
            Motion(dir, it.substring(2).toInt())
        }
    }
}
