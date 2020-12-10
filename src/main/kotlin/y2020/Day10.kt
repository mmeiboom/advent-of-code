package y2020

import lib.Day
import lib.combinations
import lib.resourceLines

object Day10 : Day {

    private val input = resourceLines(2020, 10).map { it.toInt() }

    override fun part1(): Long {
        val adapters = input.toMutableList()
        adapters.add(0)
        adapters.add(adapters.max()!! + 3)

        val deltas = adapters.sorted()
                .zipWithNext()
                .map { pair -> pair.second - pair.first }
                .groupBy { it }

        return deltas[1]!!.size * deltas[3]!!.size.toLong()
    }

    override fun part2(): Long {
        val adapters = mutableListOf<Int>()
        adapters.add(0)
        adapters.addAll(input.sorted())
        adapters.add(adapters.max()!! + 3)

        var totalArrangements = 1L
        var prevSplit = 0
        var pos = 1

        while (pos <= adapters.size) {
            if (pos == adapters.size || (adapters[pos] - adapters[pos - 1]) == 3) {
                val sublist = adapters.subList(prevSplit, pos)
                totalArrangements *= countArrangements(sublist)
                prevSplit = pos
            }
            pos++
        }

        return totalArrangements
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

    fun isValid(list: List<Int>): Boolean {
        val valid = list.zipWithNext().none { p -> ((p.second - p.first) > 3) || ((p.second - p.first) < 0) }

        //println("$valid: $list")
        return valid
    }
}

fun main() {
    println(Day10.part1())
    println(Day10.part2())
}