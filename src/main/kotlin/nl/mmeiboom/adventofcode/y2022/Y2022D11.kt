package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import java.math.BigInteger
import java.math.BigInteger.ZERO

object Y2022D11 : Day {

    private val gcd = (11 * 5 * 19 * 13 * 7 * 17 * 2 * 3).toBigInteger()
    private val testGcd = (23 * 13 * 17 * 19).toBigInteger()

    override fun part1(): Any {
        val monkeys = realMonkeys()
        IntRange(1, 20).forEach { _ ->
            monkeys.forEach {
                it.inspectAll()
            }
        }

        val mostActive = monkeys.sortedBy { it.throws }.takeLast(2)
        return mostActive[0].throws * mostActive[1].throws
    }

    override fun part2(): Any {
        val monkeys = realMonkeys()
        IntRange(1, 10000).forEach { _ ->
            monkeys.forEach {
                it.inspectAll(reduceWorry = false)
            }
        }

        val mostActive = monkeys.sortedBy { it.throws }.takeLast(2)
        return mostActive[0].throws.toLong() * mostActive[1].throws.toLong()
    }

    private fun testMonkeys(): List<Monkey> {
        // Starting situation
        val monkeys = listOf(
            Monkey(
                id = 0,
                items = mutableListOf(79.toBigInteger(), 98.toBigInteger()),
                divisor = 23,
                operation = { x: BigInteger -> x.multiply(19.toBigInteger()) }
            ),
            Monkey(
                id = 1,
                items = mutableListOf(54.toBigInteger(), 65.toBigInteger(), 75.toBigInteger(), 74.toBigInteger()),
                divisor = 19,
                operation = { x: BigInteger -> x.add(6.toBigInteger()) }
            ),
            Monkey(
                id = 2,
                items = mutableListOf(79.toBigInteger(), 60.toBigInteger(), 97.toBigInteger()),
                divisor = 13,
                operation = { x: BigInteger -> x.multiply(x) }
            ),
            Monkey(
                id = 3,
                items = mutableListOf(74.toBigInteger()),
                divisor = 17,
                operation = { x: BigInteger -> x.add(3.toBigInteger()) }
            ),
        )

        monkeys[0].link(monkeys[2], monkeys[3])
        monkeys[1].link(monkeys[2], monkeys[0])
        monkeys[2].link(monkeys[1], monkeys[3])
        monkeys[3].link(monkeys[0], monkeys[1])
        return monkeys
    }

    private fun realMonkeys(): List<Monkey> {
        // Starting situation
        val monkeys = listOf(
            Monkey(
                id = 0,
                items = mutableListOf(83.toBigInteger(), 88.toBigInteger(), 96.toBigInteger(), 79.toBigInteger(), 86.toBigInteger(), 88.toBigInteger(), 70.toBigInteger()),
                divisor = 11,
                operation = { x: BigInteger -> x.multiply(5.toBigInteger()) }
            ),
            Monkey(
                id = 1,
                items = mutableListOf(59.toBigInteger(), 63.toBigInteger(), 98.toBigInteger(), 85.toBigInteger(), 68.toBigInteger(), 72.toBigInteger()),
                divisor = 5,
                operation = { x: BigInteger -> x.multiply(11.toBigInteger()) }
            ),
            Monkey(
                id = 2,
                items = mutableListOf(90.toBigInteger(), 79.toBigInteger(), 97.toBigInteger(), 52.toBigInteger(), 90.toBigInteger(), 94.toBigInteger(), 71.toBigInteger(), 70.toBigInteger()),
                divisor = 19,
                operation = { x: BigInteger -> x.add(2.toBigInteger()) }
            ),
            Monkey(
                id = 3,
                items = mutableListOf(97.toBigInteger(), 55.toBigInteger(), 62.toBigInteger()),
                divisor = 13,
                operation = { x: BigInteger -> x.add(5.toBigInteger()) }
            ),
            Monkey(
                id = 4,
                items = mutableListOf(74.toBigInteger(), 54.toBigInteger(), 94.toBigInteger(), 76.toBigInteger()),
                divisor = 7,
                operation = { x: BigInteger -> x * x }
            ),
            Monkey(
                id = 5,
                items = mutableListOf(58.toBigInteger()),
                divisor = 17,
                operation = { x: BigInteger -> x.add(4.toBigInteger()) }
            ),
            Monkey(
                id = 6,
                items = mutableListOf(66.toBigInteger(), 63.toBigInteger()),
                divisor = 2,
                operation = { x: BigInteger -> x.add(6.toBigInteger()) }
            ),
            Monkey(
                id = 7,
                items = mutableListOf(56.toBigInteger(), 56.toBigInteger(), 90.toBigInteger(), 96.toBigInteger(), 68.toBigInteger()),
                divisor = 3,
                operation = { x: BigInteger -> x.add(7.toBigInteger()) }
            ),
        )

        monkeys[0].link(monkeys[2], monkeys[3])
        monkeys[1].link(monkeys[4], monkeys[0])
        monkeys[2].link(monkeys[5], monkeys[6])
        monkeys[3].link(monkeys[2], monkeys[6])
        monkeys[4].link(monkeys[0], monkeys[3])
        monkeys[5].link(monkeys[7], monkeys[1])
        monkeys[6].link(monkeys[7], monkeys[5])
        monkeys[7].link(monkeys[4], monkeys[1])
        return monkeys
    }

    class Monkey(
        val id: Int,
        val items: MutableList<BigInteger>,
        val divisor: Int,
        val operation: (BigInteger) -> BigInteger,
    ) {
        fun link(pass: Monkey, fail: Monkey) {
            this.passMonkey = pass
            this.failMonkey = fail
        }

        private fun test(level: BigInteger): Boolean {
            return level.mod(divisor.toBigInteger()).equals(ZERO)
        }

        fun inspectAll(reduceWorry: Boolean = true) {
            while (items.isNotEmpty()) {
                var level = items.removeFirst()
                level = operation(level)
                if (reduceWorry) level.mod(3.toBigInteger())
                if (test(level)) {
                    passMonkey.give(level.mod(gcd))
                } else {
                    failMonkey.give(level.mod(gcd))
                }
                throws += 1
            }
        }

        private fun give(item: BigInteger) {
            items.add(item)
        }

        private lateinit var failMonkey: Monkey
        private lateinit var passMonkey: Monkey
        internal var throws = 0
    }

}