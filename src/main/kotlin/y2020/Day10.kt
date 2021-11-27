package y2020

import lib.Day
import lib.combinations
import lib.resourceLines

object Day10 : Day {

    private val input = resourceLines(2020, 10)

    override fun part1(): Long {
        val deltas = sortedAdapters()
                .zipWithNext()
                .map { pair -> pair.second - pair.first }

        return deltas.count { it == 1 } * deltas.count { it == 3 }.toLong()
    }

    override fun part2(): Long {
        val adapters = sortedAdapters()
        var arrangements = 1L
        var prevSplit = 0

        for (pos in 1..adapters.size) {
            if (pos == adapters.size || (adapters[pos] - adapters[pos - 1]) == 3) {
                arrangements *= countArrangements(adapters.subList(prevSplit, pos))
                prevSplit = pos
            }
        }

        return arrangements
    }

    private fun sortedAdapters(): List<Int> {
        val sortedInput = input.map { it.toInt() }.sorted()
        return listOf(0).plus(sortedInput).plus(sortedInput.maxOrNull()!! + 3).sorted()
    }

    private fun countArrangements(list: List<Int>): Int {
        return generateLists(list).filter { isValid(it) }.count()
    }

    private fun generateLists(list: List<Int>): List<List<Int>> {
        if (list.size < 3) {
            return listOf(list)
        }

        val lists = mutableListOf<List<Int>>()
        val first = list[0]
        val last = list[list.size - 1]
        lists.add(listOf(first, last))

        val mutable = list.subList(1, list.size - 1)
        for (i in 1..list.size - 2) {
            mutable.combinations(i).forEach {
                val candidate = mutableListOf<Int>()
                candidate.add(first)
                candidate.addAll(it)
                candidate.add(last)
                lists.add(candidate)
            }
        }

        return lists
    }

    private fun isValid(list: List<Int>) = list
            .zipWithNext()
            .none { p -> ((p.second - p.first) > 3) || ((p.second - p.first) < 0) }
}

fun main() {
    println(Day10.part1())
    println(Day10.part2())
}
