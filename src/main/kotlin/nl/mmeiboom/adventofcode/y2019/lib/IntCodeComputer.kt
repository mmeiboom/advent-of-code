package nl.mmeiboom.adventofcode.y2019.lib

import nl.mmeiboom.adventofcode.y2019.lib.Opcode.*


class IntCodeComputer(program: List<Int>, val input: ArrayDeque<Int>, val output: ArrayDeque<Int>) {

    private val memory = program.toMutableList()
    private var pointer = 0
    private var running = true
    private val history = mutableListOf<Instruction>()

    fun run(): Int {
        while (running) {
            val instruction = Instruction(memory[pointer])
            when (instruction.code()) {
                ADD -> {
                    val a = read(instruction.mode(1), memory[pointer + 1])
                    val b = read(instruction.mode(2), memory[pointer + 2])
                    val out = memory[pointer + 3]
                    memory[out] = a + b
                    pointer += 4
                }

                MULT -> {
                    val a = read(instruction.mode(1), memory[pointer + 1])
                    val b = read(instruction.mode(2), memory[pointer + 2])
                    val out = memory[pointer + 3]
                    memory[out] = a * b
                    pointer += 4
                }

                HALT -> {
                    running = false
                }

                IN -> {
                    val value = input.removeFirst()
                    val address = memory[pointer + 1]
                    memory[address] = value
                    pointer += 2
                }

                OUT -> {
                    val value = read(instruction.mode(1), memory[pointer + 1])
                    output.addLast(value)
                    pointer += 2
                }

                JUMPIFTRUE -> {
                    val value = read(instruction.mode(1), memory[pointer + 1])
                    val jump = read(instruction.mode(2), memory[pointer + 2])
                    if (value > 0) {
                        pointer = jump
                    } else {
                        pointer += 3
                    }
                }

                JUMPIFFALSE -> {
                    val value = read(instruction.mode(1), memory[pointer + 1])
                    val jump = read(instruction.mode(2), memory[pointer + 2])
                    if (value == 0) {
                        pointer = jump
                    } else {
                        pointer += 3
                    }
                }

                LE -> {
                    val a = read(instruction.mode(1), memory[pointer + 1])
                    val b = read(instruction.mode(2), memory[pointer + 2])
                    val out = memory[pointer + 3]
                    memory[out] = if (a < b) 1 else 0
                    pointer += 4
                }

                EQ -> {
                    val a = read(instruction.mode(1), memory[pointer + 1])
                    val b = read(instruction.mode(2), memory[pointer + 2])
                    val out = memory[pointer + 3]
                    memory[out] = if (a == b) 1 else 0
                    pointer += 4
                }
            }
            history.add(instruction)
        }

        return memory[0]
    }

    fun read(position: Int) = memory[position]

    fun read(mode: Mode, position: Int): Int {
        return when (mode) {
            Mode.IMMEDIATE -> position
            Mode.POSITION -> memory[position]
        }
    }

}

data class Instruction(val raw: Int) {

    val digits = raw.toString().map { it.toString().toInt() }

    fun code() = Opcode.of(raw % 100)
    fun mode(param: Int): Mode {
        val modeVal = digits.getOrNull(digits.size - param - 2) ?: 0
        return when (modeVal) {
            0 -> Mode.POSITION
            1 -> Mode.IMMEDIATE
            else -> error("Invalid parameter mode")
        }
    }
}

enum class Mode {
    IMMEDIATE, POSITION
}

enum class Opcode(val code: Int) {
    ADD(1),
    MULT(2),
    IN(3),
    OUT(4),
    JUMPIFTRUE(5),
    JUMPIFFALSE(6),
    LE(7),
    EQ(8),
    HALT(99);

    companion object {
        fun of(i: Int): Opcode {
            return Opcode.values().single { it.code == i }
        }
    }
}