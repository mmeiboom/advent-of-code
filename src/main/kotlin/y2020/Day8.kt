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
        return instructions.mapIndexed { index, _ ->
            computer.load(mutatedProgram(index))
            computer.run()
            if (computer.ranSuccessfully()) computer.result() else -1L
        }.max()!!
    }

    private fun mutatedProgram(index: Int): MutableList<Instruction> {
        val mutation = instructions.toMutableList()
        if (instructions[index].operation == JMP) {
            mutation[index] = Instruction(NOP, instructions[index].argument)
        }
        if (instructions[index].operation == NOP) {
            mutation[index] = Instruction(JMP, instructions[index].argument)
        }
        return mutation
    }
}

fun main() {
    println(Day8.part1())
    println(Day8.part2())
}