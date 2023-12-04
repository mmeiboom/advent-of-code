import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceString
import nl.mmeiboom.adventofcode.lib.stringToDigits
import kotlin.math.absoluteValue

object Day16 : Day {

    val digits = stringToDigits(resourceString(2019, 16))
    val patterns = generatePatterns(digits.size)
    val offset = 5970927

    override fun part1(): Any {
        var lastPhase = digits
        for (i in 0 until 100) {
            lastPhase = calculateNextPhase(lastPhase)
        }
        return lastPhase.subList(0, 8).joinToString("")
    }

    override fun part2(): Any {
        // input size = 650
        // repeat size K = 6.500.000
        // offset N - 1 = 5.970.927
        val signal = IntRange(1, 10000).flatMap { digits }.subList(offset, 650 * 10000)

        // Therefore, base pattern simplifies to '1' for each digit to calculate
        // The digits of our signal therefore become:
        // D0 = sum(N.....K) % 10
        // D1 = sum(N+1...K) % 10
        // D2 = sum(N+2...K) % 10
        // D3 = sum(N+3...K) % 10
        // D4 = sum(N+4...K) % 10
        // D5 = sum(N+5...K) % 10
        // D6 = sum(N+6...K) % 10
        // D7 = sum(N+7...K) % 10

        println(signal.size)

        var lastPhase = signal.reversed()
        for (i in 0 until 100) {
            var previousSum = 0
            lastPhase = lastPhase.mapIndexed{index, value ->
                previousSum += value
                previousSum % 10
            }
        }
        return lastPhase.reversed().subList(0, 8).joinToString("")
    }

    private fun calculateNextPhase(digits: List<Int>): List<Int> {
        return digits.mapIndexed { index, digit ->
            val products = digits.zip(patterns[index]) { a, b -> a * b }
            products.sum().rem(10).absoluteValue
        }
    }

    private fun generatePatterns(size: Int): List<List<Int>> {
        return IntRange(1, size).map { patternFor(it, size) }
    }

    private fun patternFor(position: Int, size: Int): List<Int> {
        val pattern = mutableListOf<Int>()
        while (pattern.size < size + 1) {
            pattern.addAll(generateSequence { 0 }.take(position).toList())
            pattern.addAll(generateSequence { 1 }.take(position).toList())
            pattern.addAll(generateSequence { 0 }.take(position).toList())
            pattern.addAll(generateSequence { -1 }.take(position).toList())
        }

        return pattern.subList(1, size + 1)
    }

}

fun main() {
    println(Day16.part1())
    println(Day16.part2())
}