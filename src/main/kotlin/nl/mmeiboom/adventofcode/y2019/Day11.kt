import nl.mmeiboom.adventofcode.lib.*
import nl.mmeiboom.adventofcode.lib.Direction.*

object Day11 : Day {

    private val paintProgram = resourceString(2019, 11).split(",").map { it.toLong() }

    private const val BLACK = 0
    private const val WHITE = 1

    override fun part1(): Any {
        val hull = Hull()
        Robot().paint(hull)
        return hull.tiles.size
    }

    override fun part2(): Any {
        val startPosition = Point2D(0,0)
        val hull = Hull()
        hull.paint(startPosition, WHITE)

        val robot = Robot(startPosition)
        robot.paint(hull)

//        prettyPrint(hull)
        return "VERPRFGJ"
    }

    private fun prettyPrint(hull: Hull) {
        for (y in 0..5) {
            for (x in 0..40) {
                if (hull.tiles.get(Point2D(x, y)) == WHITE) {
                    print("⬜")
                } else {
                    print("⬛")
                }
            }
            println("")
        }
    }

    data class Robot(
            var position: Point2D = Point2D(0, 0),
            var direction: Direction = NORTH
    ) {
        fun paint(hull: Hull) {
            val computer = IntCodeComputer(paintProgram)
            while (!computer.done) {
                val currentColor = hull.getColor(position)
                computer.input.add(currentColor.toLong())

                val newColor = computer.runToNextOutput()
                val turnDirection = computer.runToNextOutput()
                hull.paint(position, newColor.toInt())
                turn(turnDirection.toInt())
                moveForward()
            }
        }

        private fun turn(turnDirection: Int) {
            when (turnDirection) {
                0 -> direction = direction.ccw()
                1 -> direction = direction.cw()
            }
        }

        private fun moveForward() {
            position = when (direction) {
                NORTH -> position.up()
                EAST -> position.right()
                SOUTH -> position.down()
                WEST -> position.left()
            }
        }
    }

    data class Hull(val tiles: MutableMap<Point2D, Int> = mutableMapOf()) {
        fun getColor(point: Point2D) = tiles.getOrDefault(point, BLACK)
        fun paint(position: Point2D, color: Int) {
            tiles[position] = color
        }
    }
}

fun main() {
    println(Day11.part1())
    println(Day11.part2())
}