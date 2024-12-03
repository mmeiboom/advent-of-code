package nl.mmeiboom.adventofcode.y2024

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.y2024.Y2024D03.InstructionType.*

object Y2024D03 : Day {

    val input = resourceLines(2024, 3)
    val regex = "mul\\((\\d+),(\\d+)\\)".toRegex()

    override fun part1(): Long {
        return input.sumOf { line ->
            regex.findAll(line).sumOf {
                it.groups[1]!!.value.toLong() * it.groups[2]!!.value.toLong()
            }
        }
    }

    override fun part2() : Long {
        return input.sumOf { resultsFor(it) }
    }

    private fun resultsFor(line: String): Long {
        var enabled = true
        var sum = 0L
        line.indices.forEach { index ->
            val instruction = parseInstruction(line.substring(index))
            when(instruction.type) {
                DO -> enabled = true
                DONT -> enabled = false
                MULT -> if(enabled) sum += instruction.value!!
                NONE -> { /* NOOP */ }
            }
        }
        return sum
    }

    private fun parseInstruction(partial: String): Instruction {
        try {
            if (partial.substring(0, 4) == "do()") {
                return Instruction(DO)
            }
            if (partial.substring(0, 7) == "don't()") {
                return Instruction(DONT)
            }
        } catch (_: IndexOutOfBoundsException) {}

        val match = regex.find(partial)
        if(match != null && match.range.first == 0) {
            return Instruction(MULT, match.groups[1]!!.value.toLong() * match.groups[2]!!.value.toLong())
        }
        return Instruction(NONE)
    }

    data class Instruction(val type: InstructionType, val value: Long? = null)
    enum class InstructionType { DO, DONT, MULT, NONE }
}

fun main() {
    println(Y2024D03.part1())
    println(Y2024D03.part2())
}