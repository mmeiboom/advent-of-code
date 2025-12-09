package nl.mmeiboom.adventofcode.y2025.day9

import nl.mmeiboom.adventofcode.lib.Line
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.Rectangle
import nl.mmeiboom.adventofcode.lib.Solution
import nl.mmeiboom.adventofcode.lib.combinations
import kotlin.math.absoluteValue

class MovieTheater(fileName: String?) : Solution<Point2D, Long>(fileName) {
    override fun parse(line: String): Point2D {
        return Point2D.of(line)
    }

    override fun solve1(data: List<Point2D>): Long {
        return data.combinations(2).map {
            val x = (it[0].x - it[1].x).absoluteValue + 1
            val y = (it[0].y - it[1].y).absoluteValue + 1
            x.toLong() * y.toLong()
        }.max()
    }

    override fun solve2(data: List<Point2D>): Long {
        val polygon = (data.zipWithNext() + listOf(Pair(data.last(), data.first())))
            .map { Line(it.first, it.second) }

        return data.combinations(2)
            .map { Rectangle(it[0], it[1]) }
            .sortedByDescending { it.surface() }
            .first { insidePolygon(polygon, it) }
            .surface()
    }

    private fun insidePolygon(
        polygon: List<Line>,
        rectangle: Rectangle,
    ) = polygon.all { rectangle.doesNotIntersect(it) }
}