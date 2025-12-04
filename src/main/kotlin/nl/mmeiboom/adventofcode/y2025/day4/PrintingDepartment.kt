package nl.mmeiboom.adventofcode.y2025.day4

import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.Solution

class PrintingDepartment(fileName: String?) : Solution<String, Int>(fileName) {

    override fun parse(line: String) = line

    override fun solve1(data: List<String>): Int {
        val map = data.parse()
        return map.removeRolls()
    }

    override fun solve2(data: List<String>): Int {
        var map = data.parse()
        var totalRemoved = 0
        while (true) {
            val removed = map.removeRolls()
            if (removed > 0) {
                totalRemoved += removed
            } else {
                break
            }
        }

        return totalRemoved
    }
}

private fun MutableMap<Point2D, Char>.removeRolls(): Int {
    val candidates = this.keys.filter { key ->
        this[key] == '@' && key.neighbors().count { this[it] == '@' } <= 3
    }
    candidates.forEach { this[it] = '.' }
    return candidates.size
}

private fun List<String>.parse(): MutableMap<Point2D, Char> {
    val map: MutableMap<Point2D, Char> = mutableMapOf()
    this.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            map[Point2D(x, y)] = char
        }
    }
    return map
}