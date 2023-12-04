package nl.mmeiboom.adventofcode.y2023

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.pow

object Y2023D04 : Day {

    val input = resourceLines(2023, 4)

    override fun part1() = input.parse()
        .sumOf { it.points() }

    override fun part2(): Int {
        val cards = input.parse()
        val counts = mutableMapOf<Int, Int>()
        cards.forEachIndexed { i, card ->
            // count the base cards itself
            counts[i] = counts.getOrDefault(i, 0) + 1
            // increment the next ones
            if(card.nrOfMatches() > 0) {
                val copies = counts.getOrDefault(i, 0)
                for(j in 1..card.nrOfMatches()) {
                    val idx = i + j
                    if(idx < cards.size) {
                        counts[idx] = counts.getOrDefault(idx, 0) + copies
                    }
                }
            }
        }

        return counts.values.sum()
    }

    data class Card(val winning: Set<Int>, val numbers: Set<Int>) {
        fun nrOfMatches() = numbers.intersect(winning).size

        fun points() : Int {
            val count = numbers.intersect(winning).size
            return if(count == 0) {
                0
            } else {
                2.0.pow(count - 1.0).toInt()
            }
        }
    }

    private fun List<String>.parse(): List<Card> {
        return this.map { line ->
            val (id, numbers) = line.split(":")
            val (winning, own) = numbers.split("|")
            val winningNumbers = winning.split(' ').mapNotNull {
                if(it.trim().isEmpty()) null else it.trim().toInt()
            }.toSet()
            val ownNumbers = own.split(' ').mapNotNull {
                if(it.trim().isEmpty()) null else it.trim().toInt()
            }.toSet()
            Card(winningNumbers, ownNumbers)
        }
    }
}

fun main() {
    print(Y2023D04.part2())
}