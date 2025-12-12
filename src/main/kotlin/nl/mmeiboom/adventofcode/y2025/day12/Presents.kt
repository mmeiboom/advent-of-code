package nl.mmeiboom.adventofcode.y2025.day12

import nl.mmeiboom.adventofcode.lib.Solution


class Presents(fileName: String?) : Solution<String, Int>(fileName) {

    override fun parse(line: String): String {
        return line
    }

    val regex = "(\\d+)x(\\d+): ([(,)\\s\\d]*)".toRegex()

    override fun solve1(data: List<String>) =
        data.filter { regex.matches(it) }
            .count { fits(it) }


    // No part 2 on Day 12
    override fun solve2(data: List<String>) = -1

    /**
     * Checks if presents can be fit into a given area
     *
     * This implementation is silly, but it works for the given input.
     * In reality, it should only have worked to filter out obvious areas that won't fit for pruning
     */
    private fun fits(line: String): Boolean {
        val (w, h, counts) = regex.matchEntire(line)!!.destructured
        val presents = counts.split(" ").map { it.toLong() }

        return w.toLong() * h.toLong() >= presents.sumOf { it * 9 }
    }
}