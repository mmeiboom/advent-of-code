import lib.Day
import lib.IntCodeComputer
import lib.permute
import lib.resourceString
import kotlin.math.max

object Day7 : Day {

    private val testProgram = resourceString(2019, 7).split(",").map { it.toLong() }

    override fun part1(): Any {
        val permutations = permute(listOf(0L, 1L, 2L, 3L, 4L))
        var bestResult = 0L

        for (perm in permutations) {
            var lastResult = 0L
            val computers = initializeAmplifiers()
            for (i in 0..4) {
                val computer = computers[i]
                computer.input.clear()
                computer.input.addAll(listOf(perm[i], lastResult))
                computer.run()
                lastResult = computer.output.last()
            }
            bestResult = max(lastResult, bestResult)
        }
        return bestResult
    }

    override fun part2(): Any {
        val permutations = permute(listOf(5L, 6, 7, 8, 9))
        var bestResult = 0L

        for (perm in permutations) {
            var lastResult = 0L
            val computers = initializeAmplifiers(perm)
            var done = false

            while (!done) {
                for (i in 0..4) {
                    val computer = computers[i]
                    if (!computer.done) {
                        computer.input.add(lastResult)
                        lastResult = computer.runToNextOutput()
                        if (i == 4) {
                            bestResult = max(lastResult, bestResult)
                        }
                    }

                    done = computer.done
                }
            }
        }
        return bestResult
    }

    private fun initializeAmplifiers(perm: List<Long>): List<IntCodeComputer> {
        val computers = initializeAmplifiers()
        for (i in 0..4) {
            computers[i].input.add(perm[i])
        }
        return computers
    }

    private fun initializeAmplifiers(): List<IntCodeComputer> {
        return listOf(
                IntCodeComputer(testProgram),
                IntCodeComputer(testProgram),
                IntCodeComputer(testProgram),
                IntCodeComputer(testProgram),
                IntCodeComputer(testProgram)
        )
    }
}

fun main() {
    println(Day7.part1())
    println(Day7.part2())
}