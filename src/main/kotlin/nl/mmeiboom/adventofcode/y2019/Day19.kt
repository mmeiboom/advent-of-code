import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.IntCodeComputer
import nl.mmeiboom.adventofcode.lib.Point
import nl.mmeiboom.adventofcode.lib.resourceString

object Day19 : Day {

    private val program = resourceString(2019, 19).split(",").map { it.toLong() }

    override fun part1(): Any {
        val points = IntRange(0, 49).flatMap { x ->
            IntRange(0, 49).map { y ->
                Point(x, y)
            }
        }

        return points.filter { hasTraction(it) }.size
    }

    private fun hasTraction(point: Point): Boolean {
        val computer = IntCodeComputer(program)
        computer.input.add(point.x.toLong())
        computer.input.add(point.y.toLong())
        val output = computer.runToNextOutput()

        return output == 1L
    }

    override fun part2(): Any {

        var bottomLeft = Point(0, 99)

        while(true) {
            while(!hasTraction(bottomLeft)) {
                bottomLeft = bottomLeft.right()
            }

            var topRight = bottomLeft.right(99).up(99)
            if(hasTraction(topRight)) {
                return bottomLeft.x * 10000 + topRight.y
            }

            bottomLeft = bottomLeft.down()
        }
    }
}

fun main() {
    println(Day19.part1())
    println(Day19.part2())
}