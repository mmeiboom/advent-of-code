package nl.mmeiboom.adventofcode.y2024

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2024D05 : Day {

    val input = resourceLines(2024, 5)

    val rules = extractRules()

    val updates = extractUpdates()

    override fun part1() = updates
        .filter { it.satisfies(rules) }
        .sumOf { it.middlePage() }

    override fun part2() = updates
        .filterNot { it.satisfies(rules) }
        .map { it.reorderToSatisfy(rules) }
        .sumOf { it.middlePage() }

    data class PrintingRule(val first: Long, val second: Long)
    data class Update(val pages: List<Long>) {
        fun satisfies(rules: List<PrintingRule>) = satisfies(pages, rules)

        private fun satisfies(pages: List<Long>, rules: List<PrintingRule>) =
            rules.all { satisfies(pages, it) }

        private fun satisfies(pages: List<Long>, rule: PrintingRule): Boolean {
            if (!pages.contains(rule.first)) {
                return true
            }
            if (!pages.contains(rule.second)) {
                return true
            }
            return pages.indexOf(rule.first) < pages.indexOf(rule.second)
        }

        fun middlePage(): Long {
            return pages[pages.size / 2]
        }

        fun reorderToSatisfy(rules: List<PrintingRule>): Update {
            val reordered = mutableListOf<Long>()
            pages.forEach { pageToInsert ->
                reordered.forEachIndexed { index, page ->
                    if(rules.contains(PrintingRule(pageToInsert, page))) {
                        reordered.add(index, pageToInsert)
                        return@forEach
                    }
                }
                reordered.add(pageToInsert)
            }
            return Update(reordered.toList())
        }
    }

    private fun extractRules() = input
        .filter { Regex("\\d+\\|\\d+").matches(it) }
        .map { rule ->
            val (first, second) = rule.split("|").map { it.toLong() }
            PrintingRule(first, second)
        }

    private fun extractUpdates() = input
        .filter { Regex("\\d+(,\\d+)*").matches(it) }
        .map { update ->
            Update(update.split(",").map { it.toLong() })
        }
}

fun main() {
    println(Y2024D05.part1())
    println(Y2024D05.part2())
}