import lib.Day
import lib.IntCodeComputer
import lib.resourceString

object Day5 : Day {

    override fun part1(): Any {
        val testProgram = resourceString(2019, 5).split(",").map { it.toLong() }
        val computer = IntCodeComputer(testProgram)
        computer.input.add(1)
        computer.run()
        return computer.output.last()
    }

    override fun part2(): Any {
        val testProgram = resourceString(2019, 5).split(",").map { it.toLong() }
        val computer = IntCodeComputer(testProgram)
        computer.input.add(5)
        computer.run()
        return computer.output.last()
    }
}