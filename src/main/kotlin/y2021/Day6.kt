package y2021

import lib.Day
import lib.resourceString

object Day6 : Day {

    private val input = resourceString(2021, 6).split(',')
        .map { it.trim().toInt() }
        .groupBy { it }
        .mapValues { it.value.size.toLong() }

    override fun part1() = runSimulation(80)
    override fun part2() = runSimulation(256)

    private fun runSimulation(daysToRun: Int): Long {
        var state = input
        for (day in 0 until daysToRun) {
            val newState = mapOf(
                0 to state.getOrDefault(1, 0L),
                1 to state.getOrDefault(2, 0L),
                2 to state.getOrDefault(3, 0L),
                3 to state.getOrDefault(4, 0L),
                4 to state.getOrDefault(5, 0L),
                5 to state.getOrDefault(6, 0L),
                6 to state.getOrDefault(0, 0L) + state.getOrDefault(7, 0L),
                7 to state.getOrDefault(8, 0L),
                8 to state.getOrDefault(0, 0L)
            )
            state = newState
        }
        return state.values.sumOf { it }
    }
}

fun main() {
    println(Day6.part1())
    println(Day6.part2())
}
