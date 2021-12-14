package y2021

import lib.Day
import lib.resourceLines

object Day14 : Day {

    private val input = resourceLines(2021, 14)

    override fun part1(): Int {
        val (template, rules) = parseInput()
        var lastString = template
        for (step in 1..10) {
            var newString = ""
            lastString.indices.forEach {
                newString = if (it < lastString.length - 1) {
                    val pair = "${lastString[it]}${lastString[it + 1]}"
                    val insertionChar = rules.getValue(pair)
                    "$newString${lastString[it]}$insertionChar"
                } else {
                    "$newString${lastString[it]}"
                }
            }
            lastString = newString
        }
        val counts = lastString.toCharArray()
            .groupBy { it }
            .map { it.key to it.value.size }
            .toMap()

        return (counts.values.maxOrNull() ?: -1) - (counts.values.minOrNull() ?: -1)
    }

    override fun part2(): Long {
        return -1
    }

    private fun parseInput(): Pair<String, Map<String, Char>> {
        val template = input.first()
        val rules = input.subList(2, input.size)
            .map { it.substring(0, 2) to it.last() }
            .toMap()
        return Pair(template, rules)
    }
}

fun main() {
    println(Day14.part1())
    println(Day14.part2())
}
