import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point
import nl.mmeiboom.adventofcode.lib.resourceLines
import java.math.BigInteger

object Day24 : Day {

    private val initialMap = toPoints(resourceLines(2019, 24))

    override fun part1(): Any {

        val previousRatings = mutableListOf<BigInteger>()
        var currentMap = initialMap
        var ratingForCurrentMap = rate(currentMap)

        while (!previousRatings.contains(ratingForCurrentMap)) {
//            printMap(currentMap)
            previousRatings.add(ratingForCurrentMap)
            currentMap = updateMap(currentMap)
            ratingForCurrentMap = rate(currentMap)
        }

        return ratingForCurrentMap
    }

    private fun printMap(currentMap: Map<Point, Char>) {
        for(y in 0 until 5) {
            for(x in 0 until 5) {
                print(currentMap.getValue(Point(x,y)))
            }
            println()
        }
        println("-------")
    }

    private fun updateMap(currentMap: Map<Point, Char>): Map<Point, Char> {
        return currentMap.mapValues { toNewValue(it, currentMap) }
    }

    private fun toNewValue(it: Map.Entry<Point, Char>, currentMap: Map<Point, Char>): Char {
        val adjacentBugs = it.key.neighborsHv().count { p -> currentMap.getOrDefault(p, '.') == '#' }

        if(it.value == '#' && adjacentBugs != 1) {
            return '.'
        }

        if(it.value =='.' && (adjacentBugs == 1 || adjacentBugs == 2)) {
            return '#'
        }

        return it.value
    }

    private fun rate(currentMap: Map<Point, Char>): BigInteger {
        var rate: BigInteger = BigInteger.valueOf(0)

        currentMap
                .filterValues { it == '#' }
                .forEach {
                    rate = rate.add(BigInteger.valueOf(2).pow(it.key.y * 5 + it.key.x))
                }

        return rate
    }

    private fun toPoints(mapLines: List<String>): Map<Point, Char> {
        val points = mutableMapOf<Point, Char>()
        mapLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                points[Point(x, y)] = char
            }
        }
        return points
    }

    private fun generatePoints(): List<Point> {
        return IntRange(0, 4).flatMap { x ->
            IntRange(0, 4).map { y ->
                Point(x, y)
            }
        }
    }

    override fun part2(): Any {
        return "nyi"
    }

}

fun main() {
    println(Day24.part1())
    println(Day24.part2())
}