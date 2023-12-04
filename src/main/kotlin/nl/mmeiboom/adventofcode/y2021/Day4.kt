package nl.mmeiboom.adventofcode.y2021

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day4 : Day {

    private val input = resourceLines(2021, 4).filter { it.isNotBlank() }

    override fun part1() : Long {
        val numbers = input.first().split(',').map { it.toInt() }
        val numberOfBoards = (input.size - 1) / 5
        val boards = IntRange(0, numberOfBoards - 1).map{ boardFromInput(it) }

        for(i in numbers.indices) {
            val marked = numbers.subList(0, i).toSet()
            boards.forEach { board ->
                if(board.hasWinningSet(marked)) {
                    return board.unmarked(marked).sum().toLong() * marked.last()
                }
            }
        }

        return -1
    }

    private fun boardFromInput(board: Int) : BingoBoard {
        val numbers = IntRange(0, 4).flatMap {
            input[board * 5 + 1 + it].trim().split("\\s+".toRegex()).map { num -> num.toInt() }
        }
        return BingoBoard(numbers)
    }

    class BingoBoard(
        private val numbers: List<Int>
    ) {
        fun hasWinningSet(markedNumbers: Set<Int>) : Boolean {
            // check rows
            for(i in 0 until 5) {
                if(
                    markedNumbers.contains(numbers[i * 5])
                    && markedNumbers.contains(numbers[i * 5 + 1])
                    && markedNumbers.contains(numbers[i * 5 + 2])
                    && markedNumbers.contains(numbers[i * 5 + 3])
                    && markedNumbers.contains(numbers[i * 5 + 4])
                ) {
                    return true
                }
            }
            // check cols
            for(i in 0 until 5) {
                if(
                    markedNumbers.contains(numbers[i])
                    && markedNumbers.contains(numbers[i + 5])
                    && markedNumbers.contains(numbers[i + 10])
                    && markedNumbers.contains(numbers[i + 15])
                    && markedNumbers.contains(numbers[i + 20])
                ) {
                    return true
                }
            }

            return false
        }

        fun unmarked(markedNumbers: Set<Int>) : Set<Int> {
            return numbers.toSet() - markedNumbers
        }

    }

    override fun part2() : Long {
        val numbers = input.first().split(',').map { it.toInt() }
        val numberOfBoards = (input.size - 1) / 5
        val boards = IntRange(0, numberOfBoards - 1).map{ boardFromInput(it) }.toMutableList()

        val completed = mutableSetOf<Int>()
        for(i in numbers.indices) {
            val marked = numbers.subList(0, i).toSet()
            boards.indices.forEach {
                if (!completed.contains(it) && boards[it].hasWinningSet(marked)) {
                    completed.add(it)
                }
                if (boards.size == completed.size) {
                    val sum = boards[it].unmarked(marked).sum().toLong()
                    return sum * marked.last()
                }
            }
        }

        return -1
    }
}

fun main() {
    println(Day4.part1())
    println(Day4.part2())
}
