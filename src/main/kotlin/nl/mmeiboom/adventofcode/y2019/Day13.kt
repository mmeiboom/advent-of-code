import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.IntCodeComputer
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceString

object Day13 : Day {

    private const val PADDLE = 3L;
    private const val BALL = 4L;

    private val game = resourceString(2019, 13).split(",").map { it.toLong() }

    override fun part1(): Any {
        val computer = IntCodeComputer(game)
        computer.run()
        return computer.output.filterIndexed { index, value -> index.rem(3) == 2 && value == 2L }.count()
    }

    override fun part2(): Any {
        val computer = IntCodeComputer(game).also { it.memory.set(0, 2) }

        var paddle = Point2D(-1, -1)
        var ball = Point2D(-1, -1)
        var score = 0L

        while (!computer.done) {
            while (!computer.done && computer.output.size < 3) computer.doCalculation()

            if (!computer.done) {
                val point = Point2D(computer.output.removeAt(0), computer.output.removeAt(0))
                val element = computer.output.removeAt(0)

                when {
                    element == PADDLE -> paddle = point
                    element == BALL -> {
                        ball = point

                        when {
                            ball.x > paddle.x -> computer.input += 1L
                            ball.x < paddle.x -> computer.input += -1L
                            else -> computer.input += 0L
                        }
                    }
                    point == Point2D(-1, 0) -> score = element
                }
            }
        }

        return score
    }

}

fun main() {
    println(Day13.part1())
    println(Day13.part2())
}