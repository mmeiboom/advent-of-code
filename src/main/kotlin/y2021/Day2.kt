package y2021

import lib.Day
import lib.resourceLines

object Day2 : Day {

    private val input = resourceLines(2021, 2)

    override fun part1() : Long {
        var horizontal = 0L
        var depth = 0L

        input.forEach {
            val (dir, delta) = it.split(' ')
            when(dir) {
                "forward" -> horizontal += delta.toLong()
                "up" -> depth -= delta.toLong()
                "down" -> depth += delta.toLong()
            }
        }

        return horizontal * depth

    }

    override fun part2() : Long {
        var horizontal = 0L
        var depth = 0L
        var aim = 0L

        input.forEach {
            val (dir, delta) = it.split(' ')
            when(dir) {
                "forward" -> {
                    horizontal += delta.toLong()
                    depth += aim * delta.toLong()
                }
                "up" -> aim -= delta.toLong()
                "down" -> aim += delta.toLong()
            }
        }

        return horizontal * depth
    }

}

fun main() {
    println(Day2.part1())
    println(Day2.part2())
}
