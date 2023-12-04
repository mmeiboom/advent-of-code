import nl.mmeiboom.adventofcode.lib.*

object Day21 : Day {

    private val program = resourceString(2019, 21).split(",").map { it.toLong() }

    override fun part1(): Any {
        val computer = IntCodeComputer(program)
        computer.writeLine("NOT C T")
        computer.writeLine("NOT A J")
        computer.writeLine("AND D T")
        computer.writeLine("OR T J")
        computer.writeLine("WALK")
        computer.run()

        // Debug on low output
        if(computer.output.last() < 1000) {
            computer.output.forEach { print(it.toChar()) }
        }

        return computer.output.last()
    }

    override fun part2(): Any {
        val computer = IntCodeComputer(program)
        computer.writeLine("NOT C T")
        computer.writeLine("NOT B J")
        computer.writeLine("OR T J")
        computer.writeLine("NOT A T")
        computer.writeLine("OR T J")
        computer.writeLine("OR E T")
        computer.writeLine("OR H T")
        computer.writeLine("AND D T")
        computer.writeLine("AND T J")
        computer.writeLine("RUN")
        computer.run()

        // Debug on low output
        if(computer.output.last() < 1000) {
            computer.output.forEach { print(it.toChar()) }
        }

        return computer.output.last()
    }

}

fun main() {
    println(Day21.part1())
    println(Day21.part2())
}