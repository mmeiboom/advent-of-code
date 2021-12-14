package y2021

import lib.Day
import lib.resourceLines

object Day14 : Day {

    private val input = resourceLines(2021, 14)
    private val template = input.first()
    private val rules = parseInput()

    override fun part1() = run(10)

    override fun part2() = run(40)

    private fun run(steps: Int): Long {

        val counts = rules.keys.associateWith { 0L }.toMutableMap()
        template.zipWithNext().forEach { (a, b) -> counts["$a$b"] = counts.getValue("$a$b") + 1 }

        for (step in 1..steps) {
            val before = counts.toMap()
            counts.keys.forEach { counts[it] = 0 }
            before.keys.forEach {
                if (before.getValue(it) > 0) {
                    val insert = rules.getValue(it)
                    val left = "${it[0]}$insert"
                    val right = "$insert${it[1]}"

                    counts[left] = before.getValue(it) + counts.getOrDefault(left, 0)
                    counts[right] = before.getValue(it) + counts.getOrDefault(right, 0)
                }
            }
        }

        val charCounts = mutableMapOf<Char, Long>()
        charCounts[template.first()] = 1
        charCounts[template.last()] = charCounts.getOrDefault(template.last(), 0) + 1
        for((pair, n) in counts) {
            for (c in pair) {
                charCounts[c] = charCounts.getOrDefault(c, 0) + n
            }
        }

        return (charCounts.values.maxOrNull()!! - charCounts.values.minOrNull()!!) / 2
    }

    private fun parseInput(): Map<String, Char> {
        return input.drop(2).associate { it.substring(0, 2) to it.last() }
    }
}

fun main() {
    println(Day14.part1())
    println(Day14.part2())
}
