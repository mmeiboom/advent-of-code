package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.y2022.Y2022D21.Operator.*

object Y2022D21 : Day {

    private val jobs = resourceLines(2022, 21).parse()

    override fun part1(): Any {
        val queue = ArrayDeque<Job>()
        val results = HashMap<String, Long>()
        jobs.filter { it.value is NumberJob }.forEach { results[it.key] = it.value.resolve(emptyList()) }
        queue.add(jobs.getValue("root"))

        while (!results.contains("root")) {
            val job = queue.first()
            if (job.inputs().all { results.contains(it) }) {
                // All preconditions met, calculate!
                queue.removeFirst()
                val values = job.inputs().map { results.getValue(it) }
                results[job.monkey()] = job.resolve(values)
            } else {
                // Schedule calculating precondition first
                job.inputs()
                    .filterNot { results.contains(it) }
                    .forEach {
                        queue.addFirst(jobs.getValue(it))
                    }
            }
        }

        return results.getValue("root")
    }

    interface Expression {
        fun resolve(): Long
    }

    data class PrimitiveExpression(val value: Long) : Expression {
        override fun resolve() = value
    }

    data class BinaryExpression(val left: Expression, val right: Expression, val operator: Operator) : Expression {
        override fun resolve(): Long {
            return operator.apply(left.resolve(), right.resolve())
        }
    }

    enum class Operator(val sign: Char) {
        MINUS('-'),
        PLUS('+'),
        MULTIPLY('*'),
        DIVISION('/'),
        EQUALS('=');

        fun apply(left: Long, right: Long): Long {
            return when (this) {
                MINUS -> left - right
                PLUS -> left + right
                MULTIPLY -> left * right
                DIVISION -> left / right
                EQUALS -> TODO()
            }
        }

        companion object {
            fun of(sign: Char): Operator {
                return Operator.values().first { it.sign == sign }
            }
        }
    }

    override fun part2(): Any {
        val remaining = jobs.values.toMutableList()
        val unsolved = mutableMapOf<String, Expression>()
        while (remaining.isNotEmpty()) {
            val nextIndex = remaining.indexOfFirst { job ->
                job is NumberJob ||
                        job.inputs().all { unsolved.contains(it) }
            }
            val job = remaining.removeAt(nextIndex)
            if (job is NumberJob) {
                unsolved[job.monkey()] = PrimitiveExpression(job.number)
            } else if (job is EvalJob) {
                val left = unsolved.getValue(job.left)
                val right = unsolved.getValue(job.right)
                unsolved[job.monkey()] = BinaryExpression(left, right, job.operator)
            }
        }

        var done = false
        val solved = mutableMapOf<String, Long>()
        while (!done) {
            val solvable = unsolved.filter {
                val exp = it.value
                val solvable =
                    if (exp is PrimitiveExpression) {
                        exp.value != 855L // Can't solve HUMN
                    } else {
                        // YUCK
                        val job = jobs.getValue(it.key) as EvalJob
                        job.monkey != "root" && solved.contains(job.left) && solved.contains(job.right)
                    }
                solvable
            }

            done = solvable.isEmpty()

            solvable.forEach {
                val exp = unsolved.remove(it.key)!!
                if (exp is PrimitiveExpression) {
                    solved[it.key] = exp.value
                }
                if (exp is BinaryExpression) {
                    val job = jobs.getValue(it.key) as EvalJob
                    val left = solved.getValue(job.left)
                    val right = solved.getValue(job.right)
                    val result = exp.operator.apply(left, right)
                    solved[it.key] = result
                }
            }
        }

        printSimplified("root", unsolved, solved, jobs)
        println()
        return -1
    }

    private fun printSimplified(
        key: String,
        unsolved: Map<String, Expression>,
        solved: MutableMap<String, Long>,
        jobs: Map<String, Job>
    ) {

        if (key == "humn") {
            print("x")
        } else {
            val job = jobs.getValue(key) as EvalJob
            print("(")
            if (solved.contains(job.left)) {
                print(solved.getValue(job.left))
            } else {
                printSimplified(job.left, unsolved, solved, jobs)
            }
            if(key == "root") {
                print("=")
            } else {
                print(job.operator.sign)
            }
            if (solved.contains(job.right)) {
                print(solved.getValue(job.right))
            } else {
                printSimplified(job.right, unsolved, solved, jobs)
            }
            print(")")
        }

    }

    interface Job {
        fun resolve(inputs: List<Long>): Long
        fun inputs(): List<String>
        fun monkey(): String
    }

    data class NumberJob(val monkey: String, val number: Long) : Job {
        override fun resolve(inputs: List<Long>) = number
        override fun inputs() = emptyList<String>()
        override fun monkey() = monkey
    }

    data class EvalJob(val monkey: String, val left: String, val right: String, val operator: Operator) : Job {
        override fun resolve(inputs: List<Long>): Long {
            val (left, right) = inputs
            return when (operator) {
                PLUS -> left + right
                MINUS -> left - right
                DIVISION -> left / right
                MULTIPLY -> left * right
                else -> throw Error()
            }
        }

        override fun inputs() = listOf(left, right)
        override fun monkey() = monkey
    }

    private fun List<String>.parse(): Map<String, Job> {
        return this.associate { line ->
            val monkey = line.substring(0, 4)
            val pair = if (line.length > 10) {
                val left = line.substring(6, 10)
                val right = line.substring(13, 17)
                val operator = line.substring(11, 12)
                monkey to EvalJob(monkey, left, right, Operator.of(operator.toCharArray().first()))
            } else {
                monkey to NumberJob(monkey, line.substring(6).toLong())
            }
            pair
        }
    }
}

fun main() {
//    val p1 = Y2022D21.part1()
//    println("P1: $p1")
    val p2 = Y2022D21.part2()
    println("P2: $p2")

}