package nl.mmeiboom.adventofcode.y2025.day12

import nl.mmeiboom.adventofcode.lib.Solution

/**
 * Advent of Code 2025, Day 11 - Reactor
 * Problem Description: http://adventofcode.com/2025/day/11
 * Blog Post/Commentary: https://todd.ginsberg.com/post/advent-of-code/2025/day11/
 */
class Presents(fileName: String?) : Solution<String, Long>(fileName) {

    override fun parse(line: String): String {
        return line
    }

    val regex = "(\\d+)x(\\d+): ([(,)\\s\\d]*)".toRegex()

    override fun solve1(data: List<String>): Long {
        return data.filter {
            regex.matches(it)
        }
            .count { fits(it) }
            .toLong()
    }

    private fun fits(line: String): Boolean {
        val (w, h, counts) = regex.matchEntire(line)!!.destructured
        val presents = counts.split(" ").map { it.toLong() }
        return w.toLong() * h.toLong() >= presents.sumOf { it * 9 }
    }

    override fun solve2(data: List<String>): Long {
        return -1
    }
}