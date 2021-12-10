package y2021

import lib.Day
import lib.Stack
import lib.combinations
import lib.resourceLines
import y2021.Day10.score
import java.lang.Math.floor

object Day10 : Day {

    private val input = resourceLines(2021, 10)

    override fun part1() : Int {
        return input.sumOf { score(it) }
    }

    fun score (line: String) : Int {
        val stack = Stack<Char>()
        val scores = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137,
        )
        val openers = mapOf(
            ')' to '(',
            ']' to '[',
            '}' to '{',
            '>' to '<',
        )
        line.forEach {
            if(listOf('{','[','<','(').contains(it)) {
                stack.push(it)
            } else {
                val expected = stack.pop()
                if(expected != openers.getValue(it)) {
                    return scores.getValue(it)
                }
            }
        }
        return 0
    }

    fun missingChars (line: String) : Stack<Char> {
        val stack = Stack<Char>()
        val openers = mapOf(
            ')' to '(',
            ']' to '[',
            '}' to '{',
            '>' to '<',
        )
        line.forEach {
            if(listOf('{','[','<','(').contains(it)) {
                stack.push(it)
            } else {
                stack.pop()
            }
        }
        return stack
    }

    private fun scoreStack(stack: Stack<Char>) : Long {
        var score = 0L
        val scores = mapOf(
            '(' to 1L,
            '[' to 2L,
            '{' to 3L,
            '<' to 4L,
        )
        while(!stack.isEmpty()) {
            score *= 5L
            score += scores.getValue(stack.pop()!!)
        }
        return score
    }

    override fun part2() : Long {
        val scores = input
            .filter { score(it) == 0 }
            .map { missingChars(it) }
            .map { scoreStack(it) }
            .sorted()

        return scores[scores.size / 2]
    }
}

fun main() {
    println(Day10.part1())
    println(Day10.part2())
}
