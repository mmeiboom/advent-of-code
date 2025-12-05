package nl.mmeiboom.adventofcode.y2025.day5

import nl.mmeiboom.adventofcode.lib.Solution
import kotlin.math.max
import kotlin.math.min

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

        val ranges = data
            .takeWhile { it.isNotBlank() }
            .map {
                val (from, to) = it.split("-")
                Range(from.toLong(), to.toLong())
            }

        val merged = combine(emptyList(), ranges)
        return merged.sumOf { it.length }
    }

    data class Range(val lower: Long, val upper: Long) {
        val length = upper - lower + 1

        fun overlapsWith(other: Range): Boolean {
            return this.upper >= other.lower && this.lower <= other.upper
        }

        fun combinedWith(other: Range): Range {
            return Range(
                min(this.lower, other.lower),
                max(this.upper, other.upper)
            )
        }
    }

    tailrec fun combine(merged: List<Range>, remaining: List<Range>): List<Range> {
        if(remaining.isEmpty()) return merged

        val next = remaining.first()
        val into = merged.find { it.overlapsWith(next) }
        if(into == null) {
            return combine(merged + next, remaining - next)
        } else {
            val combined = next.combinedWith(into)
            return combine(merged - into, remaining - next + combined)
        }
    }
}