package nl.mmeiboom.adventofcode.y2025.day7

import nl.mmeiboom.adventofcode.lib.Matrix
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.Solution
import nl.mmeiboom.adventofcode.lib.Stack

class Laboratories(fileName: String?) : Solution<String, Long>(fileName) {

    override fun parse(line: String) = line

    override fun solve1(data: List<String>): Long {
        val map = Matrix.from(data).toMutableMap()
        val start = map.keys.first { map[it] == 'S' }

        val stack = Stack<Point2D>()
        stack.push(start)
        while (!stack.isEmpty()) {
            val next = stack.pop()!!
            val down = next.down()
            if (map[down] == '^') {
                stack.push(down.left())
                stack.push(down.right())
            } else if (map[down] == '.') {
                map[down] = '|'
                stack.push(down)
            }
        }

        return map.keys.count { map[it] == '^' && map[it.up()] == '|' }.toLong()
    }

    override fun solve2(data: List<String>): Long {
        val map = Matrix.from(data).toMutableMap()
        val counts = mutableMapOf<Point2D, Long>()
        val start = map.keys.first { map[it] == 'S' }
        map[start.down()] = '|'
        counts[start.down()] = 1

        for(y in 1 until data.size) {
            for(x in 0 until data[0].length) {
                val point = Point2D(x, y)
                val down = point.down()
                val char = map[point]

                if(char == '|') {
                    val countsToPoint = counts.getValue(point)
                    if(map[down] == '.') {
                        map[down] = '|'
                    }
                    if(map[down] == '^') {
                        map[down.left()] = '|'
                        map[down.right()] = '|'
                        counts[down.left()] = counts.getOrDefault(down.left(), 0) + countsToPoint
                        counts[down.right()] = counts.getOrDefault(down.right(), 0) + countsToPoint
                    }
                    counts[down] = counts.getOrDefault(down, 0L) + countsToPoint
                }
            }
        }

        return (0 until data[0].length).sumOf {
            counts.getOrDefault(Point2D(it, 141), 0L)
        }


    }

}