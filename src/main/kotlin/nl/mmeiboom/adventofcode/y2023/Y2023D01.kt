package nl.mmeiboom.adventofcode.y2023

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.max

object Y2023D01 : Day {

    val input = resourceLines(2023, 1)

    override fun part1() = input.sumOf { it.calibrationValue() }

    override fun part2() = input.sumOf { it.extendedCalibrationValue() }


    private fun String.calibrationValue(): Int {
        val firstDigit = this.first { it.isDigit() }
        val lastDigit = this.last { it.isDigit() }

        return "$firstDigit$lastDigit".toInt()
    }

    private val digitString = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "zero"
    )

    private fun String.extendedCalibrationValue(): Int {
        // First
        var firstDigit: Char? = null
        var firstIndex = this.length
        this.forEachIndexed { index, char ->
            if (char.isDigit() && index < firstIndex) {
                firstDigit = char
                firstIndex = index
            }
        }

        digitString.forEachIndexed { index, literal ->
            val firstIndexOfLiteral = this.indexOf(literal)
            if (firstIndexOfLiteral > -1 && firstIndexOfLiteral < firstIndex) {
                firstIndex = firstIndexOfLiteral
                firstDigit = (index + 1).toString().first()
            }
        }

        // Last
        var lastDigit: Char? = null
        var lastIndex = 0
        this.forEachIndexed { index, char ->
            if (char.isDigit()) {
                lastDigit = char
                lastIndex = max(index, lastIndex)
            }
        }

        digitString.forEachIndexed { index, literal ->
            val lastIndexOfLiteral = this.lastIndexOf(literal)
            if (lastIndexOfLiteral > lastIndex) {
                lastIndex = lastIndexOfLiteral
                lastDigit = (index + 1).toString().first()
            }
        }

        return "$firstDigit$lastDigit".toInt()
    }

}

fun main() {
    print(Y2023D01.part2())
}