package lib

class Computer() {

    private var instructions = emptyList<Instruction>()
    private var visited = mutableSetOf<Int>()
    private var acc = 0L
    private var pos = 0
    private var terminated = false
    private var looping = false

    fun load(program: List<Instruction>) {
        instructions = program
    }

    fun run() {
        reset()
        while (!terminated && !looping) {
            applyInstruction()
        }
    }

    private fun applyInstruction() {
        val instruction = instructions[pos]
        pos += instruction.shift()
        if (visited.contains(pos)) {
            looping = true
        }
        terminated = pos >= instructions.size
        acc += instruction.offset()
        visited.add(pos)
    }

    private fun reset() {
        pos = 0
        acc = 0L
        terminated = false
        looping = false
        visited.clear()
    }

    fun ranSuccessfully() = terminated && !looping

    fun result(): Long {
        return acc
    }

    data class Instruction(val operation: Operation, val argument: Int) {

        companion object {
            fun fromString(s: String): Instruction {
                val (op, arg) = s.split(" ")
                return Instruction(Operation.valueOf(op.toUpperCase()), arg.toInt())
            }
        }

        fun offset() =
                when (operation) {
                    Operation.ACC -> argument
                    Operation.NOP -> 0
                    Operation.JMP -> 0
                }

        fun shift() =
                when (operation) {
                    Operation.ACC -> 1
                    Operation.NOP -> 1
                    Operation.JMP -> argument
                }
    }

    enum class Operation {
        NOP, ACC, JMP
    }
}