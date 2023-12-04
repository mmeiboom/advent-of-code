import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.IntCodeComputer
import nl.mmeiboom.adventofcode.lib.resourceString

object Day2 : Day {

    private val crashedInput = resourceString(2019, 2).split(",").map { it.toLong() }

    override fun part1(): Any {
        val correctedInput = crashedInput.mapIndexed { i, v ->
            when (i) {
                1 -> 12
                2 -> 2
                else -> v
            }
        }

        val computer = IntCodeComputer(correctedInput)
        return computer.runToNextOutput()
    }

    override fun part2(): Any {
        for (noun in 0..99) for (verb in 0..99) {
            val correctedInput = crashedInput.mapIndexed { i, v ->
                when (i) {
                    1 -> noun.toLong()
                    2 -> verb.toLong()
                    else -> v
                }
            }

            val computer = IntCodeComputer(correctedInput)
            val output = computer.runToNextOutput()
            if (output == 19690720L) {
                return 100 * noun + verb
            }
        }
        return "No result"
    }

}
