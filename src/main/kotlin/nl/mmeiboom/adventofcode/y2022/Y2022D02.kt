package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.y2022.Y2022D02.Move.*

object Y2022D02 : Day {

    val input = resourceLines(2022, 2)
        .map { line -> line.split(' ')}

    val winningMoves = mapOf(
        ROCK to SCISSORS,
        PAPER to ROCK,
        SCISSORS to PAPER,
    )

    val losingMoves = mapOf(
        SCISSORS to ROCK,
        ROCK to PAPER,
        PAPER to SCISSORS,
    )

    enum class Move(val points: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        companion object {
            fun decode(value: List<String>): List<Move> {
                return value.map {
                    when (it) {
                        "A", "X" -> ROCK
                        "B", "Y" -> PAPER
                        "C", "Z" -> SCISSORS
                        else -> throw Error()
                    }
                }
            }

            fun decode2(value: List<String>): List<Move> {
                val left = when(value[0]) {
                    "A" -> ROCK
                    "B" -> PAPER
                    "C" -> SCISSORS
                    else -> throw Error()
                }

                val right = when(value[1]) {
                    "X" -> winningMoves.getValue(left)
                    "Y" -> left
                    "Z" -> losingMoves.getValue(left)
                    else -> throw Error()
                }

                return listOf(left, right)
            }
        }
    }

    private fun score(opponentMove: Move, playerMove: Move): Int {
        if (opponentMove == playerMove) {
            return playerMove.points + 3
        }

        if (winningMoves[opponentMove] == playerMove) {
            return playerMove.points
        }
        return playerMove.points + 6
    }

    override fun part1(): Any {
        return input
            .map { Move.decode(it) }
            .sumOf { (a, b) -> score(a, b) }
    }

    override fun part2(): Any {
        return input
            .map { Move.decode2(it) }
            .sumOf { (a, b) -> score(a, b) }
    }

}