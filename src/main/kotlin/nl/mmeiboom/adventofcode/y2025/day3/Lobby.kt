package nl.mmeiboom.adventofcode.y2025.day3

import nl.mmeiboom.adventofcode.lib.Solution

class Lobby(fileName: String?) : Solution<String, Long>(fileName) {
    override fun parse(line: String): String {
        return line
    }

    override fun solve1(data: List<String>): Long {
        return data.sumOf { joltage(it, 2).toLong() }
    }

    fun joltage(line: String, batteries: Int): String {
        if (batteries == 0) return ""
        val next = line.dropLast(batteries - 1).max()
        val lineWithoutNext = line.dropWhile { it != next }.drop(1)
        val remainder = joltage(lineWithoutNext, batteries - 1)
        return "$next$remainder"
    }

    override fun solve2(data: List<String>): Long {
        return data.sumOf { joltage(it, 12).toLong() }
    }


}