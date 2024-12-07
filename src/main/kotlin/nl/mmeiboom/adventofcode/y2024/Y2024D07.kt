package nl.mmeiboom.adventofcode.y2024

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2024D07 : Day {

    val input = resourceLines(2024, 7).map { it.toEquation() }

    override fun part1() = input.filter { it.hasSolution() }.sumOf { it.testValue }

    override fun part2() = -1

    private fun String.toEquation(): Equation {
        val (testValue, remainder) = this.split(":")
        val numbers = remainder.trim().split(' ').map { it.trim().toLong() }
        return Equation(testValue.toLong(), numbers)
    }

    data class Equation(val testValue: Long, val numbers: List<Long>) {

        fun hasSolution() : Boolean {
            return hasSolution(testValue, listOf(numbers[0]), numbers.subList(1, numbers.size))
        }

        private fun hasSolution(testValue: Long, results: List<Long>, numbers: List<Long>) : Boolean {
            if(numbers.isEmpty()) return results.contains(testValue)
            val head = numbers[0]
            val tail = numbers.subList(1, numbers.size)
            val newResults = results.flatMap {
                listOf(it + head, it * head, "$it$head".toLong())
            }

            return hasSolution(testValue, newResults, tail)
        }

    }
}

fun main() {
    println(Y2024D07.part1())
    println(Y2024D07.part2())
}