package nl.mmeiboom.adventofcode.y2020

import nl.mmeiboom.adventofcode.lib.Computer
import nl.mmeiboom.adventofcode.lib.Computer.ExitCode.TERMINATED
import nl.mmeiboom.adventofcode.lib.Computer.Instruction
import nl.mmeiboom.adventofcode.lib.Computer.Operation.JMP
import nl.mmeiboom.adventofcode.lib.Computer.Operation.NOP
import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day8 : Day {

    private val program = resourceLines(2020, 8).map { Instruction.fromString(it) }

    override fun part1(): Long {
        val computer = Computer()
        computer.load(program)
        computer.run()
        return computer.result()
    }

    override fun part2(): Long {
        val computer = Computer()

        return program.withIndex()
                .filter { it.value.operation in listOf(JMP, NOP) }
                .map { mutatedProgram(it.index) }
                .map {
                    if (computer.run(it) == TERMINATED) computer.result() else -1L
                }.maxOrNull()!!
    }

    private fun mutatedProgram(index: Int): List<Instruction> {
        return program.mapIndexed { idx, entry ->
            when {
                idx != index -> entry
                entry.operation == JMP -> entry.copy(operation = NOP)
                entry.operation == NOP -> entry.copy(operation = JMP)
                else -> entry
            }
        }
    }
}

fun main() {
    println(Day8.part1())
    println(Day8.part2())
}
