package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Direction
import nl.mmeiboom.adventofcode.lib.Direction.*
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.y2022.Y2022D22.TurnDirection.LEFT
import nl.mmeiboom.adventofcode.y2022.Y2022D22.TurnDirection.RIGHT
import nl.mmeiboom.adventofcode.y2022.Y2022D22.WrappingMode.CUBE
import nl.mmeiboom.adventofcode.y2022.Y2022D22.WrappingMode.MAP

object Y2022D22 : Day {

    private val input = resourceLines(2022, 22)

    override fun part1(): Any {
        return solve(MAP)
    }

    override fun part2(): Any {
        return solve(CUBE)
    }

    private fun solve(mode: WrappingMode): Int {
        val (grove, instructions) = input.parse()
        grove.mode = mode
        instructions.forEach { it.apply(grove) }
        grove.print()
        return (1000 * (grove.position.y + 1)) + (4 * (grove.position.x + 1)) + grove.orientation.value()
    }

    class Grove(val map: MutableMap<Point2D, Char>) {
        fun moveForward() {
            val nextNonEmpty = nextNonEmpty()
            if (".<>^v".contains(map.getValue(nextNonEmpty))) {
                map[position] = orientation.displayChar()
                position = nextNonEmpty
            }
        }

        fun print() {
            val red = "\u001b[31m"
            val reset = "\u001b[0m"

            println()
            for (y in 0..maxY) {
                for (x in 0 until maxX) {
                    val char = map.getOrDefault(Point2D(x, y), ' ')
                    if ("<>^v".contains(char)) {
                        print("$red$char$reset")
                    } else {
                        print(char)
                    }
                }
                println()
            }
        }

        private fun nextNonEmpty(): Point2D {
            var position = position.plus(orientation).wrap(mode)
            while (!map.contains(position)) {
                position = position.plus(orientation).wrap(mode)
            }
            return position
        }

        private fun Point2D.wrap(mode: WrappingMode): Point2D {
            if (mode == MAP) {
                if (this.x > maxX) {
                    return Point2D(0, y)
                }
                if (this.x < 0) {
                    return Point2D(maxX, y)
                }
                if (this.y > maxY) {
                    return Point2D(x, 0)
                }
                if (this.y < 0) {
                    return Point2D(x, maxY)
                }
            }

            if (mode == CUBE) {
                // Face dimensions: 50x50

                // Faces unfolded:
                // .12
                // .3.
                // 45.
                // 6..

                // Plane dimensions of the cube


                // What plane are we in?

                // If on the edge: what is the next point?


            }

            return this
        }

        var mode: WrappingMode = MAP
        var orientation: Direction = EAST
        var position: Point2D = map.keys.filter { it.y == 0 }.minBy { it.x }
        private val maxX = map.keys.maxOf { it.x }
        private val maxY = map.keys.maxOf { it.y }
    }

    enum class WrappingMode {
        MAP, CUBE
    }

    enum class TurnDirection {
        LEFT, RIGHT;

        companion object {
            fun of(char: Char): TurnDirection =
                when (char) {
                    'L' -> LEFT
                    'R' -> RIGHT
                    else -> throw Error("Unexpected input '$char'")
                }
        }
    }

    interface Instruction {
        fun apply(grove: Grove)
    }

    data class MoveInstruction(val distance: Int) : Instruction {
        override fun apply(grove: Grove) {
            repeat(distance) {
                grove.moveForward()
            }
        }
    }

    data class TurnInstruction(val turnDirection: TurnDirection) : Instruction {
        override fun apply(grove: Grove) {
            grove.orientation = when (turnDirection) {
                LEFT -> grove.orientation.ccw()
                RIGHT -> grove.orientation.cw()
            }
        }
    }

    private fun Direction.value() = when (this) {
        EAST -> 0
        SOUTH -> 1
        WEST -> 2
        NORTH -> 3
    }

    private fun Direction.displayChar() = when (this) {
        EAST -> '>'
        SOUTH -> 'v'
        WEST -> '<'
        NORTH -> '^'
    }

    private fun List<String>.parse(): Pair<Grove, List<Instruction>> {

        val map: MutableMap<Point2D, Char> = mutableMapOf()
        val mapLines = this.takeWhile { it.isNotEmpty() }
        mapLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if ("#.".contains(char)) {
                    map[Point2D(x, y)] = char
                }
            }
        }

        var line = this.last()
        val instructions = mutableListOf<Instruction>()
        while (line.isNotEmpty()) {
            val numericPart = line.takeWhile { it != 'L' && it != 'R' }
            line = line.drop(numericPart.length)
            if (numericPart.isNotEmpty()) {
                instructions.add(MoveInstruction(numericPart.toInt()))
            }
            val turnPart = line.takeWhile { it == 'L' || it == 'R' }
            line = line.drop(turnPart.length)
            if (turnPart.isNotEmpty()) {
                instructions.add(TurnInstruction(TurnDirection.of(turnPart[0])))
            }
        }

        return Pair(
            Grove(map),
            instructions
        )
    }


}

fun main() {
    val p1 = Y2022D22.part1()
    println("P1: $p1")
//    val p2 = Y2022D22.part2()
//    println("P2: $p2")
}


