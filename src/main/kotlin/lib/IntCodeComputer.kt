package lib

import lib.IntCodeComputer.Opcode.*

/**
 * Kotlin version of JavaIntCodeComputer
 *
 * This version was not written by me, but served as a comparison while debuggingthe Java version.
 */
class IntCodeComputer(
        val memory: Memory,
        val input: MutableList<Long> = mutableListOf(),
        val output: MutableList<Long> = mutableListOf()
) {
    var done = false
    var ip = 0;
    var rb = 0;

    constructor(program: List<Long>) :
            this(Memory(program.mapIndexed { index, l -> index.toLong() to l }.toMap().toMutableMap()))

    constructor(program: List<Long>, input: MutableList<Long>) :
            this(Memory(program.mapIndexed { index, l -> index.toLong() to l }.toMap().toMutableMap()), input)

    fun run() {
        while (!done) {
            doCalculation()
        }
    }

    fun runToNextOutput(): Long {
        val outputSizeBefore = output.size
        while (!done && outputSizeBefore == output.size) {
            doCalculation()
        }
        return output.lastOrNull() ?: -1L
    }

    fun doCalculation() {
        if (done) {
            return
        }

        val op = Opcode.from(memory.get(ip))
        val instruction = Instruction(
                op,
                (1 until op.len).map { memory.get(ip + it) },
                (memory.get(ip) / 100).toString().padStart(op.len - 1, '0').map { it - '0' }.reversed()
        )

        val result = instruction.apply(ip, rb, memory, input, output)
        if (result == null) {
            done = true
        } else {
            ip = result.first
            rb = result.second
        }
    }

    fun writeLine(line: String) {
        line.forEach { input.add(it.toLong()) }
        input.add('\n'.toLong())
    }

    fun readLine(): String {
        val s = mutableListOf<Char>()
        while (true) {
            if (output.isEmpty()) {
                runToNextOutput()
            }
            val char = output.removeAt(0).toChar()
            if (char == '\n') {
                return s.joinToString("")
            }
            s.add(char)
        }
    }

    data class Memory(val memory: MutableMap<Long, Long>) {
        fun get(pos: Int) = get(pos.toLong())
        fun get(pos: Long) = memory.computeIfAbsent(pos) { 0 }
        fun set(pos: Long, value: Long) {
            memory[pos] = value
        }

        fun set(pos: Int, value: Long) {
            set(pos.toLong(), value)
        }
    }

    data class Instruction(val op: Opcode, val params: List<Long>, val modes: List<Int>) {
        fun apply(
                ip: Int,
                rb: Int,
                memory: Memory,
                input: MutableList<Long>,
                output: MutableList<Long>
        ): Pair<Int, Int>? {

            var base = rb

            fun get(param: Int) = when (modes[param]) {
                0 -> memory.get(params[param].toInt())
                1 -> params[param]
                2 -> memory.get(params[param].toInt() + base)
                else -> throw IllegalArgumentException("Invalid param $param")
            }

            fun set(param: Int, value: Long) {
                when (modes[param]) {
                    0 -> memory.set(params[param].toInt(), value)
                    2 -> memory.set(params[param].toInt() + base, value)
                }
            }

            when (op) {
                ADD -> set(2, get(0) + get(1))
                MUL -> set(2, get(0) * get(1))
                SAV -> set(0, if (input.size > 0) input.removeAt(0) else -1L)
                OUT -> output.add(get(0))
                JIT -> if (get(0) != 0L) return get(1).toInt() to rb
                JIF -> if (get(0) == 0L) return get(1).toInt() to rb
                LT -> set(2, if (get(0) < get(1)) 1 else 0)
                EQ -> set(2, if (get(0) == get(1)) 1 else 0)
                REL -> base += get(0).toInt()
                TERM -> return null
            }

            return ip + op.len to base
        }
    }

    enum class Opcode(val op: Int, val len: Int) {
        ADD(1, 4),
        MUL(2, 4),
        SAV(3, 2),
        OUT(4, 2),
        JIT(5, 3),
        JIF(6, 3),
        LT(7, 4),
        EQ(8, 4),
        REL(9, 2),
        TERM(99, 1);

        companion object {
            fun from(opcode: Long) = from(opcode.toInt())
            fun from(opcode: Int): Opcode = values().find { it.op == opcode % 100 } ?: throw IllegalArgumentException()
        }
    }

}