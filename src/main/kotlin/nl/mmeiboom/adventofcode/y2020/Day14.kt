package nl.mmeiboom.adventofcode.y2020

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day14 : Day {

    private val input = resourceLines(2020, 14)

    override fun part1(): Long {
        val matcher = Regex("mem\\[(\\d+)\\] = (\\d+)")
        var mask = ""
        val memory = mutableMapOf<Int, Long>()
        input.forEach {
            if (it.startsWith("mask")) {
                mask = it.substring(7)
            } else {
                val (address, value) = matcher.find(it)!!.destructured
                memory[address.toInt()] = combine(value, mask)
            }
        }
        return memory.values.sum()
    }

    private fun combine(value: String, mask: String): Long {
        val x = value.toLong().toString(2).padStart(36, '0')
        val stringValu = x.indices.map {
            when (mask[it]) {
                'X' -> x[it]
                else -> mask[it]
            }
        }.joinToString("")

        return stringValu.toLong(2)
    }

    override fun part2(): Long {
        val matcher = Regex("mem\\[(\\d+)\\] = (\\d+)")
        var mask = ""
        val memory = mutableMapOf<Long, Long>()
        input.forEach {
            if (it.startsWith("mask")) {
                mask = it.substring(7)
            } else {
                val (address, value) = matcher.find(it)!!.destructured
                val targets = applyMask(address, mask)
                targets.forEach { target -> memory[target] = value.toLong() }
            }
        }
        return memory.values.sum()
    }

    private fun applyMask(address: String, mask: String): List<Long> {
        val x = address.toLong().toString(2).padStart(36, '0')
        val floatingAddress = x.indices.map {
            when (mask[it]) {
                '1' -> '1'
                '0' -> x[it]
                else -> 'X'
            }
        }.joinToString("")

        val targets = explode(floatingAddress).map { it.toLong(2) }
        return targets
    }

    private fun explode(remainder: String): List<String> {
        if (remainder.length == 1) {
            val left = if (remainder[0] == 'X' || remainder[0] == '1') listOf("1") else emptyList()
            val right = if (remainder[0] == 'X' || remainder[0] == '0') listOf("0") else emptyList()
            return left + right
        }

        val rest = explode(remainder.substring(1))
        val left = if (remainder[0] == 'X' || remainder[0] == '1') rest.map { "1$it" } else emptyList()
        val right = if (remainder[0] == 'X' || remainder[0] == '0') rest.map { "0$it" } else emptyList()
        return left + right
    }
}

fun main() {
    println(Day14.part1())
    println(Day14.part2())
}