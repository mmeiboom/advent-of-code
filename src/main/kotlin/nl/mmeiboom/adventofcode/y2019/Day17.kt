import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.IntCodeComputer
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceString

object Day17 : Day {

    private val program = resourceString(2019, 17).split(",").map { it.toLong() }

    override fun part1(): Any {
        val computer = IntCodeComputer(program)
        computer.run()
        val output = computer.output.map { l -> l.toChar() }
        val points = toPointsMap(output)

        return points.filter { e -> e.key.neighborsHv().filter { n -> points.containsKey(n) && points[n] != '.' }.size == 4 }
                .map { e -> e.key.x * e.key.y }
                .sum()
    }

    private fun toPointsMap(output: List<Char>): Map<Point2D, Char> {
        var row = 0
        var col = 0

        val scaffold = mutableMapOf<Point2D, Char>()

        output.forEach { o ->
            scaffold.put(Point2D(row, col), o)
            if (o == '\n') {
                col = 0
                row++
            } else {
                col++
            }
        }

        return scaffold
    }

    override fun part2(): Any {
        val computer = IntCodeComputer(program).also { it.memory.set(0, 2) }

        computer.writeLine("A,B,A,B,C,C,B,A,B,C")
        computer.writeLine("L,10,R,10,L,10,L,10")
        computer.writeLine("R,10,R,12,L,12")
        computer.writeLine("R,12,L,12,R,6")
        computer.writeLine("n")
        computer.run()

        val output = computer.output
        return output.last()
    }

}

fun main() {
    println(Day17.part1())
    println(Day17.part2())
}