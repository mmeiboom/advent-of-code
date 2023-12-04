package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.absoluteValue

object Y2022D10 : Day {

    override fun part1(): Any {
        val instructions = resourceLines(2022, 10).parse()
        val xOverTime = run(instructions)
        return xOverTime[20 - 1] * 20 + xOverTime[60 - 1] * 60 + xOverTime[100 - 1] * 100 + xOverTime[140 - 1] * 140 + xOverTime[180 - 1] * 180 + xOverTime[220 - 1] * 220
    }

    override fun part2(): Any {
        val instructions = resourceLines(2022, 10).parse()
        val xOverTime = run(instructions)
//        drawCRT(xOverTime)
        return "CHECK OUTPUT"
    }

    private fun drawCRT(xOverTime: List<Int>) {
        val width = 40
        val height = 6
        for (y in 0 until height) {
            var line = ""
            for (x in 0 until width) {
                val dx = (xOverTime[x + 40 * y] - x).absoluteValue
                line += if (dx <= 1) {
                    "█"
                } else {
                    " "
                }
            }
            println(line)
        }
    }

    private fun run(instructions: MutableList<Instruction>): MutableList<Int> {
        val xOverTime = mutableListOf(1)
        var cycle = 0
        while (cycle < 240 && instructions.isNotEmpty()) {
            var newX = xOverTime[cycle]
            instructions.first().tick()
            if (instructions.first().ready()) {
                val instruction = instructions.removeFirst()
                newX = instruction.apply(newX)
            }
            xOverTime.add(newX)
            cycle += 1
        }
        return xOverTime
    }

    abstract class Instruction(private var time: Int) {
        fun tick() {
            time -= 1
        }

        fun ready() = time <= 0
        abstract fun apply(x: Int): Int
    }

    class AddInstruction(private val value: Int) : Instruction(2) {
        override fun apply(x: Int) = x + value
    }

    class NoopInstruction : Instruction(1) {
        override fun apply(x: Int) = x
    }

    private fun List<String>.parse(): MutableList<Instruction> {
        return this.map {
            when {
                it.startsWith("noop") -> NoopInstruction()
                it.startsWith("addx") -> AddInstruction(it.substring(5).toInt())
                else -> throw Error()
            }
        }.toMutableList()
    }

}