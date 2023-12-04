package nl.mmeiboom.adventofcode.y2021

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day11 : Day {

    private val input = resourceLines(2021, 11)

    override fun part1(): Long {
        val cavern = input.parseGrid()
        var flashCount = 0L
        for (step in 1..100) {
            cavern.values.forEach { it.increment() }
            var shouldFlash = cavern.entries.find { it.value.shouldFlash() }
            while (shouldFlash != null) {
                flashCount++
                shouldFlash.value.flash()
                shouldFlash.key.neighbors().forEach { cavern[it]?.increment() }
                shouldFlash = cavern.entries.find { it.value.shouldFlash() }
            }
            cavern.values.forEach { it.reset() }
        }
        return flashCount
    }

    override fun part2(): Long {
        val cavern = input.parseGrid()
        var step = 0L
        while (true) {
            step++
            cavern.values.forEach { it.increment() }
            var shouldFlash = cavern.entries.find { it.value.shouldFlash() }
            while (shouldFlash != null) {
                shouldFlash.value.flash()
                shouldFlash.key.neighbors().forEach { cavern[it]?.increment() }
                shouldFlash = cavern.entries.find { it.value.shouldFlash() }
            }
            if (cavern.values.all { it.flashed }) {
                return step
            }
            cavern.values.forEach { it.reset() }
        }
    }

    private fun printGrid(cavern: Map<Point, Octopus>) {
        val max = cavern.keys.maxOrNull() ?: return
        for (i in 0..max.y) {
            for (j in 0..max.x) {
                val octopus = cavern.getValue(Point(j, i))
                print("${octopus.energy}")
            }
            print("\n")
        }
    }

    class Octopus(var energy: Int) {
        var flashed: Boolean = false

        fun increment() {
            energy += 1
        }

        fun flash() {
            flashed = true
        }

        fun reset() {
            flashed = false
            if (energy > 9) {
                energy = 0
            }
        }

        fun shouldFlash(): Boolean {
            return energy > 9 && !flashed
        }
    }

    private fun List<String>.parseGrid(): Map<Point, Octopus> {
        val area: MutableMap<Point, Octopus> = mutableMapOf()
        this.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                area[Point(x, y)] = Octopus(char - '0')
            }
        }
        return area
    }
}


fun main() {
    println(Day11.part1())
    println(Day11.part2())
}
