package nl.mmeiboom.adventofcode.y2024

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.y2024.Y2024D07.Operator.PLUS
import nl.mmeiboom.adventofcode.y2024.Y2024D07.Operator.MULTIPLY
import nl.mmeiboom.adventofcode.y2024.Y2024D07.Operator.CONCATENATE

object Y2024D07 : Day {

    val input = resourceLines(2024, 7).map { it.toEquation() }

    override fun part1() = input.filter { it.hasSolution(listOf(PLUS, MULTIPLY)) }.sumOf { it.testValue }

    override fun part2() = input.filter { it.hasSolution(listOf(PLUS, MULTIPLY, CONCATENATE)) }.sumOf { it.testValue }

    private fun String.toEquation(): Equation {
        val (testValue, remainder) = this.split(":")
        val numbers = remainder.trim().split(' ').map { it.trim().toLong() }
        return Equation(testValue.toLong(), numbers)
    }

    data class Equation(val testValue: Long, val numbers: List<Long>) {

        fun hasSolution(operators: List<Operator>) : Boolean {
            return hasSolution(operators, testValue, listOf(numbers[0]), numbers.subList(1, numbers.size))
        }

        private fun hasSolution(operators: List<Operator>, testValue: Long, results: List<Long>, numbers: List<Long>) : Boolean {
            if(numbers.isEmpty()) return results.contains(testValue)
            val head = numbers[0]
            val tail = numbers.subList(1, numbers.size)
            val newResults = results.flatMap {
                operators.map { operator -> operator.biFunction.invoke(it, head) }
            }

            return hasSolution(operators, testValue, newResults, tail)
        }

    }

    enum class Operator(val biFunction: (Long, Long) -> Long) {
        MULTIPLY({ a, b -> a * b }),
        PLUS({ a, b -> a * b }),
        CONCATENATE({ a, b -> "$a$b".toLong() })
    }
}

fun main() {
    println(Y2024D07.part1())
    println(Y2024D07.part2())
}