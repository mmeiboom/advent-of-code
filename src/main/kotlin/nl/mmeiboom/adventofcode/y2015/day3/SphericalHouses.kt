package nl.mmeiboom.adventofcode.y2015.day3

import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.Solution

class SphericalHouses(fileName: String?) : Solution<CharArray, Int>(fileName) {
    override fun parse(line: String): CharArray {
        return line.toCharArray()
    }

    override fun solve1(data: List<CharArray>): Int {
        var currentPoint = Point2D(0, 0)
        val points = mutableSetOf(currentPoint)
        val instructions = data.single()
        instructions.forEach {
            when (it) {
                '^' -> currentPoint = currentPoint.up()
                'v' -> currentPoint = currentPoint.down()
                '>' -> currentPoint = currentPoint.right()
                '<' -> currentPoint = currentPoint.left()
            }
            points.add(currentPoint)
        }

        return points.size
    }

    override fun solve2(data: List<CharArray>): Int {
        var robot = Point2D(0, 0)
        var santa = Point2D(0, 0)
        val points = mutableSetOf(robot)
        val instructions = data.single()
        instructions.forEachIndexed { i, it ->
            if (i % 2 == 0) {
                when (it) {
                    '^' -> santa = santa.up()
                    'v' -> santa = santa.down()
                    '>' -> santa = santa.right()
                    '<' -> santa = santa.left()
                }
                points.add(santa)
            } else {
                when (it) {
                    '^' -> robot = robot.up()
                    'v' -> robot = robot.down()
                    '>' -> robot = robot.right()
                    '<' -> robot = robot.left()
                }
                points.add(robot)
            }
        }

        return points.size
    }
}