package nl.mmeiboom.adventofcode.y2020

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day7 : Day {

    private val input = resourceLines(2020, 7)

    override fun part1(): Long {
        val map = bagsTree()
        return map.keys
                .filter { canContainGoldBag(it, map) }
                .size
                .toLong()
    }

    private fun canContainGoldBag(key: String, map: Map<String, Map<String, Int>>) : Boolean {
        val contentsForKey = map[key]!!
        if(contentsForKey.containsKey("shiny gold")) {
            return true
        }
        return contentsForKey.keys.any { canContainGoldBag(it, map) }
    }

    private fun bagsTree(): Map<String, Map<String, Int>> {
        val map = input.map {
            val (bag, rest) = it.split(" bags contain ")
            val bags = """(\d+) (\w+ \w+) bags?""".toRegex().findAll(rest)
                    .map { mr -> mr.groupValues[2] to mr.groupValues[1].toInt() }
                    .toMap()

            bag to bags
        }.toMap()
        return map
    }

    override fun part2(): Long {
        val map = bagsTree()
        return countBagsIn("shiny gold", map)
    }

    private fun countBagsIn(key: String, map: Map<String, Map<String, Int>>): Long {
        val contentsForKey = map[key]!!
        return contentsForKey.map { it.value + it.value * countBagsIn(it.key, map) }.sum()
    }
}

fun main() {
    println(Day7.part1())
    println(Day7.part2())
}