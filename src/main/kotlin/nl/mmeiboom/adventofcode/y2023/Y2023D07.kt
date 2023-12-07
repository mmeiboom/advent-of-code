package nl.mmeiboom.adventofcode.y2023

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2023D07 : Day {

    val input = resourceLines(2023, 7).parse()

    override fun part1() = input
        .sortedWith(comparator(allowJokers = false))
        .mapIndexed { index, hand -> (index + 1) * hand.bid  }
        .sum()

    override fun part2() = input //.forEach { println("${it.cards.map { it.symbol }.joinToString("")} : ${it.type(true)}") }
        .sortedWith(comparator(allowJokers = true))
        .mapIndexed { index, hand -> (index + 1) * hand.bid  }
        .sum()

    data class Hand(val cards: List<Card>, val bid: Long) {

        fun type(allowJokers: Boolean): HandType {
            val counts = cards.groupBy { it.symbol }.map { it.key to it.value.size }.toMap()
            val jokers = if(allowJokers) counts.getOrDefault('J', 0) else 0
            val countsWithJokers = counts.map {
                val countWithJokers = if(it.key == 'J') it.value else it.value + jokers
                it.key to countWithJokers
            }.toMap()

            // Helpers
            val twoPairNoJokers = counts.count { it.value == 2 } == 2
            val fhNoJokers = counts.containsValue(3) && counts.containsValue(2)

            if(countsWithJokers.containsValue(5)) {
                return HandType.FIVE_OF_A_KIND
            }
            if(countsWithJokers.containsValue(4)) {
                return HandType.FOUR_OF_A_KIND
            }

            if(fhNoJokers || (jokers == 1 && twoPairNoJokers)) {
                // Full House never makes use of Jokers, as there will be a better alternative
                return HandType.FULL_HOUSE
            }
            if(countsWithJokers.containsValue(3)) {
                return HandType.THREE_OF_A_KIND
            }

            if(twoPairNoJokers) {
                // Two pair never makes use of Jokers, as there will be a better alternative
                return HandType.TWO_PAIR
            }
            if(countsWithJokers.containsValue(2)) {
                return HandType.ONE_PAIR
            }
            return HandType.HIGH_CARD
        }

        fun compareCardsTo(other: Hand, withJokers: Boolean): Int {
            this.cards.zip(other.cards).forEach { (c1, c2) ->
                val left = c1.rank(withJokers)
                val right = c2.rank(withJokers)
                if (left != right) {
                    return left - right
                }
            }
            return 0
        }

    }

    enum class HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND,
    }

    enum class Card(val symbol: Char) {
        TWO('2'),
        THREE('3'),
        FOUR('4'),
        FIVE('5'),
        SIX('6'),
        SEVEN('7'),
        EIGHT('8'),
        NINE('9'),
        T('T'),
        JACK('J'),
        QUEEN('Q'),
        KING('K'),
        ACE('A');

        fun rank(withJokers: Boolean): Int {
            return if(withJokers && this.symbol == 'J') -1 else this.ordinal
        }

        companion object {
            fun fromSymbol(char: Char): Card {
                return Card.values().first { it.symbol == char }
            }
        }
    }

    private fun List<String>.parse() : List<Hand> {
        return this.map { line ->
            val (cards, bid) = line.split(' ')
            Hand(cards.map { Card.fromSymbol(it) }, bid.toLong())
        }
    }

    private fun comparator(allowJokers: Boolean) = Comparator<Hand> { a, b ->
        val aType = a.type(allowJokers = allowJokers)
        val bType = b.type(allowJokers = allowJokers)
        when {
            aType != bType -> aType.ordinal - bType.ordinal
            else -> a.compareCardsTo(b, allowJokers)
        }
    }
}

fun main() {
    print(Y2023D07.part2())
}