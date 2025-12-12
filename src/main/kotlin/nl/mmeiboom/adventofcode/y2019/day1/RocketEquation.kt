package nl.mmeiboom.adventofcode.y2019.day1

import nl.mmeiboom.adventofcode.lib.Solution

class RocketEquation(fileName: String?) : Solution<Int, Int>(fileName) {
    override fun parse(line: String): Int {
        return line.toInt()
    }

    override fun solve1(data: List<Int>): Int {
        return data.sumOf { (it / 3) - 2 }
    }

    override fun solve2(data: List<Int>): Int {
        return data.sumOf {
            fuelFor(it)
        }
    }

    fun fuelFor(mass: Int): Int {
        val fuelForMass = (mass / 3) - 2
        if(fuelForMass <= 0) {
            return 0
        }
        return fuelForMass + fuelFor(fuelForMass)
    }

}