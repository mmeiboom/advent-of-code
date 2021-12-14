package y2021

import lib.Day
import lib.Point
import lib.resourceLines

object Day13 : Day {

    private val input = resourceLines(2021, 13)
    private val pointPattern = """\d+,\d+""".toRegex()

    override fun part1(): Int {
        val (paper, instructions) = parseInput()

        val afterOneFold = paper
                .mapNotNull { instructions[0].apply(it) }
                .distinct()
                .toSet()

        return afterOneFold.size
    }

    private fun printGrid(grid: Collection<Point>) {
        val max = grid.maxOrNull() ?: return
        for (i in 0..max.y + 1) {
            for (j in 0..max.x + 1) {
                val char = if(grid.contains(Point(j,i))) '⬜' else '⬛'
                print("$char")
            }
            print("\n")
        }
    }

    override fun part2(): Long {
        val (paper, instructions) = parseInput()
        var code = paper

        instructions.forEach { instruction ->
            code = code
                .mapNotNull { instruction.apply(it) }
                .toSet()
        }

        printGrid(code)

        return -1
    }

    private fun parseInput(): Pair<Set<Point>, List<Folder>> {
        val paper = input
            .filter { it.matches(pointPattern) }
            .map {
                val (x, y) = it.split(',')
                Point(x.toInt(), y.toInt())
            }.toSet()

        val instructions = input
            .filter { it.startsWith("fold") }
            .map {
                val (axis, position) = it.split('=')
                Folder(axis.takeLast(1), position.toInt())
            }
        return Pair(paper, instructions)
    }

    class Folder(val axis: String, val position: Int) {
        fun apply(point : Point) : Point? {
            if(axis == "x") {
                if(point.x < position) {
                    return point
                }
                if(point.x > position) {
                    return Point(point.x - (point.x - position) * 2, point.y)
                }
                return null
            } else {
                if(point.y < position) {
                    return point
                }
                if(point.y > position) {
                    return Point(point.x, point.y - (point.y - position) * 2)
                }
                return null
            }
        }
    }
}

fun main() {
    println(Day13.part1())
    println(Day13.part2())
}
