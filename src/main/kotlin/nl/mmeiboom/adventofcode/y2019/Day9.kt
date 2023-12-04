import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.IntCodeComputer
import nl.mmeiboom.adventofcode.lib.resourceString

object Day9 : Day {

    private val boostProgram = resourceString(2019, 9).split(",").map { it.toLong() }

    override fun part1(): Any {
        val computer = IntCodeComputer(boostProgram, mutableListOf(1L))
        computer.run()
        return computer.output.last()
    }

    override fun part2(): Any {
        val computer = IntCodeComputer(boostProgram, mutableListOf(2L))
        computer.run()
        return computer.output.last()
    }
}

fun main() {
    Day9.part1()
}
