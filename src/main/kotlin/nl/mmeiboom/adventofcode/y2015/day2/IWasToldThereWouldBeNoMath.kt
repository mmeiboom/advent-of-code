package nl.mmeiboom.adventofcode.y2015.day2

import nl.mmeiboom.adventofcode.lib.Solution

class IWasToldThereWouldBeNoMath(fileName: String?) : Solution<Dimension, Int>(fileName) {
    override fun parse(line: String): Dimension {
        val (l, w, h) = line.split('x').map { it.toInt() }
        return Dimension(l, w, h)
    }

    override fun solve1(data: List<Dimension>) = data.sumOf { it.wrappingSurface() }

    override fun solve2(data: List<Dimension>) = data.sumOf { it.ribbonLength() }
}

data class Dimension(val l: Int, val w: Int, val h: Int) {
    fun wrappingSurface(): Int {
        val side1 = l * w
        val side2 = w * h
        val side3 = h * l
        return 2 * side1 + 2 * side2 + 2 * side3 + minOf(side1, side2, side3)
    }

    fun ribbonLength(): Int {
        val perimeter1 = 2 * l + 2 * w
        val perimeter2 = 2 * w + 2 * h
        val perimeter3 = 2 * l + 2 * h
        val volume = l * w * h
        return minOf(perimeter1, perimeter2, perimeter3) + volume
    }

}