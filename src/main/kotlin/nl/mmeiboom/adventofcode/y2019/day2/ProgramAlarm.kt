package nl.mmeiboom.adventofcode.y2019.day2

import nl.mmeiboom.adventofcode.lib.Solution
import nl.mmeiboom.adventofcode.y2019.lib.IntCodeComputer

class ProgramAlarm(fileName: String?) : Solution<String, Int>(fileName) {
    override fun parse(line: String) = line

    override fun solve1(data: List<String>): Int {
        return runWith(data, 12, 2)
    }

    override fun solve2(data: List<String>): Int {
        for (verb in 0..99) {
            for (noun in 0..99) {
                val result = runWith(data, noun, verb)
                if (result == 19690720) {
                    return 100 * noun + verb
                }
            }
        }

        error("Impossible!")
    }

    private fun runWith(data: List<String>, noun: Int, verb: Int): Int {
        val program = data.single()
            .split(",")
            .mapIndexed({ i, s ->
                when (i) {
                    1 -> noun
                    2 -> verb
                    else -> s.toInt()
                }
            })

        val input = ArrayDeque<Int>()
        val output = ArrayDeque<Int>()
        val computer = IntCodeComputer(program, input, output)
        computer.run()
        return computer.read(0)
    }

}