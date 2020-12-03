package y2020

import lib.Day
import lib.Point
import lib.resourceLines

object Day3 : Day {

    private val map = resourceLines(2020, 3)

    override fun part1(): Long {
        return countTrees(Point(3, 1))
    }

    override fun part2(): Long {
        val slopes = listOf(
                Point(1, 1),
                Point(3, 1),
                Point(5, 1),
                Point(7, 1),
                Point(1, 2)
        )

        return slopes.map { countTrees(it)}.reduce { acc, i -> acc * i }
    }

    private fun countTrees(slope : Point) : Long {
        var trees = 0L
        var position = Point(0, 0)

        while (position.y < map.size) {
            val modX = position.x % map[position.y].length
            if (map[position.y].get(modX) == '#') {
                trees += 1
            }
            position = position.plus(slope)
        }
        return trees
    }

}

fun main() {
    println(Day3.part1())
    println(Day3.part2())
}