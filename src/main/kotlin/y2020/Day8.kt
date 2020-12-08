package y2020

import lib.Computer
import lib.Computer.Instruction
import lib.Computer.Operation.JMP
import lib.Computer.Operation.NOP
import lib.Day
import lib.resourceLines

object Day8 : Day {

    private val instructions = resourceLines(2020, 8).map { Instruction.fromString(it) }

    override fun part1(): Long {
        val computer = Computer()
        computer.load(instructions)
        computer.run()
        return computer.result()
    }

    override fun part2(): Long {
        val computer = Computer()

        return instructions.withIndex()
                .filter { it.value.operation in listOf(JMP, NOP) }
                .map { mutatedProgram(instructions, it.index) }
                .map {
                    computer.load(it)
                    computer.run()
                    if (computer.ranSuccessfully()) computer.result() else -1L
                }.max()!!
    }

    private fun mutatedProgram(instructions: List<Instruction>, index: Int): List<Instruction> {
        return instructions.mapIndexed { idx, entry ->
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