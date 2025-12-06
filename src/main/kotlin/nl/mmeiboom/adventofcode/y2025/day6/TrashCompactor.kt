package nl.mmeiboom.adventofcode.y2025.day6

import nl.mmeiboom.adventofcode.lib.Solution

class TrashCompactor(fileName: String?) : Solution<String, Long>(fileName) {

    override fun parse(line: String) = line

    override fun solve1(data: List<String>): Long {

        val inputs = data.dropLast(1).map { line ->
            line.split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        }

        val operators = data.last()
            .split(" ")
            .filter { it.isNotBlank() }

        return operators.mapIndexed { i, operator ->
            when (operator) {
                "+" -> inputs[0][i] + inputs[1][i] + inputs[2][i] + inputs[3][i]
                "*" -> inputs[0][i] * inputs[1][i] * inputs[2][i] * inputs[3][i]
                else -> throw IllegalArgumentException("Invalid operator $operator")
            }
        }.sum()
    }

    override fun solve2(data: List<String>): Long {
        val chars = data.dropLast(1).map { it.toCharArray().toList() }
        val digits = transpose(chars)
            .map { it.joinToString("").trim().toLongOrNull() }
            .dropLastWhile { it == null }

        val operators = data.last()
            .split(" ")
            .filter { it.isNotBlank() }

        var sum = 0L
        var operatorIndex = 0
        val collected = mutableListOf<Long>()

        digits.forEachIndexed{ i, it ->
            if(it != null) {
                collected.add(it)
            }

            if(it == null || i == digits.size - 1) {
                // compute
                val operator = operators[operatorIndex]
                sum += when(operator) {
                    "*" -> collected.reduce { sum, element -> sum * element }
                    "+" -> collected.reduce { sum, element -> sum + element }
                    else -> 0
                }

                // reset
                collected.clear()
                operatorIndex++
            }
        }

        return sum
    }

    fun transpose(xs: List<List<Char>>): List<List<Char>> {
        val cols = xs[0].size + 4
        val rows = xs.size
        var ys = MutableList(cols) { MutableList(rows) { ' ' } }
        for (i in 0 until rows) {
            for (j in 0 until cols)
                ys[j][i] = xs[i].getOrElse(j, {' '})
        }
        return ys
    }

    /**
     * Returns a list of lists, each built from elements of all lists with the same indexes.
     * Output has length of shortest input list.
     */
    public inline fun <T> zip(vararg lists: List<T>): List<List<T>> {
        return zip(*lists, transform = { it })
    }

    /**
     * Returns a list of values built from elements of all lists with same indexes using provided [transform].
     * Output has length of shortest input list.
     */
    public inline fun <T, V> zip(vararg lists: List<T>, transform: (List<T>) -> V): List<V> {
        val minSize = lists.map(List<T>::size).min() ?: return emptyList()
        val list = ArrayList<V>(minSize)

        val iterators = lists.map { it.iterator() }
        var i = 0
        while (i < minSize) {
            list.add(transform(iterators.map { it.next() }))
            i++
        }

        return list
    }
}