package nl.mmeiboom.adventofcode.y2024

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2024D04 : Day {

    val input = resourceLines(2024, 4).parse()
    val zero = Point2D(0, 0)

    override fun part1() = input.keys.sumOf { origin -> wordsStartingAtPoint2D(origin).count { it == "XMAS" } }

    override fun part2() = input.keys.count { origin -> wordsAroundPoint2D(origin).count { it == "MAS" } == 2 }

    private fun wordsStartingAtPoint2D(origin: Point2D): List<String> {
        val directions = zero.neighbors()
        return directions.map { d ->
            var word = ""
            var currentPoint = origin
            (0..3).forEach { _ ->
                if (input.containsKey(currentPoint)) {
                    word += input[currentPoint]
                    currentPoint += d
                }
            }
            word
        }
    }

    private fun wordsAroundPoint2D(origin: Point2D): List<String> {
        val diagonalDirections = zero.neighbors() - zero.neighborsHv()
        return diagonalDirections.map { d ->
            var word = ""
            var currentPoint = origin - d
            (0..2).forEach { _ ->
                if (input.containsKey(currentPoint)) {
                    word += input[currentPoint]
                    currentPoint += d
                }
            }
            word
        }
    }

    private fun List<String>.parse(): Map<Point2D, Char> {
        val map: MutableMap<Point2D, Char> = mutableMapOf()
        this.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                map[Point2D(x, y)] = char
            }
        }
        return map
    }
}

fun main() {
    println(Y2024D04.part1())
    println(Y2024D04.part2())
}