package nl.mmeiboom.adventofcode.y2025.day5

import nl.mmeiboom.adventofcode.lib.Solution
import kotlin.math.max

class Cafeteria(fileName: String?) : Solution<String, Long>(fileName) {

    override fun parse(line: String) = line

    override fun solve1(data: List<String>): Long {
        val ranges = data
            .takeWhile { it.isNotBlank() }
            .map {
                val (from, to) = it.split("-")
                LongRange(from.toLong(), to.toLong())
            }

        val ingredients = data.drop(ranges.size + 1)
            .map { it.toLong() }

        return ingredients.count { ranges.any { range -> range.contains(it) } }.toLong()
    }

    override fun solve2(data: List<String>): Long {

        // Sort to ensure simple merging
        val sortedRanges = data
            .takeWhile { it.isNotBlank() }
            .map {
                val (from, to) = it.split("-")
                Pair(from.toLong(), to.toLong())
            }.sortedWith(compareBy({ it.first }, { it.second }))

        // Bit verbose merging logic
        val unmerged = sortedRanges.toMutableList()
        val merged = mutableListOf<Pair<Long, Long>>()
        while (unmerged.isNotEmpty()) {
            var mergedRange = unmerged.removeFirst()
            var max = mergedRange.second
            while (unmerged.isNotEmpty() && unmerged.first().first <= max) {
                val mergeIn = unmerged.removeFirst()
                max = max(max, mergeIn.second)
                mergedRange = Pair(mergedRange.first, max)
            }
            merged.add(mergedRange)
        }

        return merged.sumOf { it.second - it.first + 1 }
    }
}