package nl.mmeiboom.adventofcode.y2019.day5

import nl.mmeiboom.adventofcode.lib.Solution
import nl.mmeiboom.adventofcode.y2019.lib.IntCodeComputer

class Asteroids(fileName: String?) : Solution<String, Int>(fileName) {
    override fun parse(line: String): String {
        return line
    }

    override fun solve1(data: List<String>): Int {
        val program = data.single()
            .split(",")
            .map { it.toInt() }

        val input = ArrayDeque<Int>()
        input.addLast(1)
        val output = ArrayDeque<Int>()
        val computer = IntCodeComputer(program, input, output)
        computer.run()
        return output.last()
    }

    override fun solve2(data: List<String>): Int {
        val program = data.single()
            .split(",")
            .map { it.toInt() }

        val input = ArrayDeque<Int>()
        input.addLast(5)
        val output = ArrayDeque<Int>()
        val computer = IntCodeComputer(program, input, output)
        computer.run()
        return output.last()
    }
}