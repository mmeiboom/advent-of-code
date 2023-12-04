package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceRegex

object Y2022D04 : Day {

    val input = resourceRegex(2022, 4, "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex())

    override fun part1(): Any {
        return input.count { (_, a1, a2, b1, b2) ->
            val aContainsB = a1.toInt() <= b1.toInt() && a2.toInt() >= b2.toInt()
            val bContainsA = b1.toInt() <= a1.toInt() && b2.toInt() >= a2.toInt()

            aContainsB || bContainsA
        }
    }

    override fun part2(): Any {
        return input.count { (_, a1, a2, b1, b2) ->
            val a = IntRange(a1.toInt(), a2.toInt()).toSet()
            val b = IntRange(b1.toInt(), b2.toInt()).toSet()
            a.intersect(b).isNotEmpty()
        }
    }

}