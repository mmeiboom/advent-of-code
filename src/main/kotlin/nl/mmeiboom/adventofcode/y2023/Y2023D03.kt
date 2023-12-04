package nl.mmeiboom.adventofcode.y2023

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.lib.toPointsMap

object Y2023D03 : Day {

    val input = resourceLines(2023, 3)
    val map = input.toPointsMap()

    override fun part1() = input
        .scanNumbers()
        .filter { it.adjacentToSymbol() }
        .sumOf { it.value() }

    override fun part2() : Int {
        val numbers = input.scanNumbers()
        val gears = input.scanGears()
        return gears
            .map { adjacentNumbers(it, numbers)}
            .filter { it.size == 2 }
            .sumOf { it[0].value() * it[1].value() }
    }

    private fun adjacentNumbers(gear: Gear, numbers: Set<Number>) : List<Number> {
        return numbers.filter { number ->
            number.points
                .flatMap { it.neighbors() }
                .contains(gear.point)
        }
    }


    data class Gear(val point: Point2D)
    data class Number(val points: List<Point2D>) {
        fun adjacentToSymbol(): Boolean {
            return this.points
                .flatMap { it.neighbors() }
                .any {
                    val char = map.getOrDefault(it, '.')
                    char != '.' && ! char.isDigit()
                }
        }

        fun value() : Int {
            return points.map { map[it] }.joinToString(separator = "").toInt()
        }
    }

    private fun List<String>.scanNumbers(): Set<Number> {
        val currentNumberPoints = mutableListOf<Point2D>()
        val numbers = mutableSetOf<Number>()
        this.forEachIndexed { y, line ->
            line.forEachIndexed { x, character ->
                if(character.isDigit()) {
                    currentNumberPoints.add(Point2D(x,y))
                } else {
                    if(currentNumberPoints.isNotEmpty()) {
                        numbers.add(Number(currentNumberPoints.toList()))
                        currentNumberPoints.clear()
                    }
                }
            }

            // EOL cleanup
            if(currentNumberPoints.isNotEmpty()) {
                numbers.add(Number(currentNumberPoints.toList()))
                currentNumberPoints.clear()
            }
        }
        return numbers
    }

    private fun List<String>.scanGears(): Set<Gear> {
        val gears = mutableSetOf<Gear>()
        this.forEachIndexed { y, line ->
            line.forEachIndexed { x, character ->
                if (character == '*') {
                    gears.add(Gear(Point2D(x, y)))
                }
            }
        }
        return gears
    }
}

fun main() {
    print(Y2023D03.part2())
}