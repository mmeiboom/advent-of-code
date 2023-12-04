package nl.mmeiboom.adventofcode.y2023

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2023D02 : Day {

    val input = resourceLines(2023, 2).map { it.toGameResults() }

    override fun part1() = input.filter {game ->
        game.draws.all {sample ->
            sample.blueCount <= 14 &&
            sample.greenCount <= 13 &&
            sample.redCount <= 12
        }
    }.sumOf { it.id }

    override fun part2() = input.sumOf {
        it.power()
    }

    private fun String.toGameResults(): GameResult {
        val (game, samples) = this.split(":")
        val gameId = game.substring(5).toInt()
        val results = samples.split(";").map { it.toGameResult() }
        return GameResult(gameId, results)
    }

    private fun String.toGameResult(): SampleCount {
        var red = 0
        var green = 0
        var blue = 0
        this.split(",").forEach {
            val (countString, color) = it.trim().split(" ")
            val count = countString.trim().toInt()
            if(color.contains("red")) {
                red = count
            } else if(color.contains("blue")) {
                blue = count
            } else if(color.contains("green")) {
                green = count
            } else {
                throw Error("unexpected color")
            }
        }
        return SampleCount(red,green,blue)
    }

    data class GameResult(
        val id: Int,
        val draws: List<SampleCount>
    ) {
        fun power() : Int {
            val minRed = draws.maxOf { it.redCount }
            val minGreen = draws.maxOf { it.greenCount }
            val minBlue = draws.maxOf { it.blueCount }
            return minRed * minGreen * minBlue
        }
    }

    data class SampleCount(val redCount: Int, val greenCount: Int, val blueCount: Int)

}

fun main() {
    print(Y2023D02.part2())
}