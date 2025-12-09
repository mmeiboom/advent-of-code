package nl.mmeiboom.adventofcode.y2025.day9

import nl.mmeiboom.adventofcode.lib.Line
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.Solution
import nl.mmeiboom.adventofcode.lib.combinations
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

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
        // All possible squares between red tiles
        val candidates = data.combinations(2).sortedByDescending {
            val x = (it[0].x - it[1].x).absoluteValue + 1
            val y = (it[0].y - it[1].y).absoluteValue + 1
            x.toLong() * y.toLong()
        }

        // Generate all edges for polygon
        val polygon = (data.zipWithNext() + listOf(Pair(data.last(), data.first())))
            .map { Line(it.first, it.second) }

        // Find first square (ordered by size) that fits inside to get our answqer
        val rectangle = candidates.first { insidePolygon(polygon, it[0], it[1]) }
        val x = (rectangle[0].x - rectangle[1].x).absoluteValue + 1
        val y = (rectangle[0].y - rectangle[1].y).absoluteValue + 1
        return x.toLong() * y.toLong()
    }

    private fun insidePolygon(
        lines: List<Line>,
        p1: Point2D,
        p2: Point2D
    ): Boolean {
        val tl = Point2D(min(p1.x, p2.x), min(p1.y, p2.y))
        val tr = Point2D(max(p1.x, p2.x), min(p1.y, p2.y))
        val bl = Point2D(min(p1.x, p2.x), max(p1.y, p2.y))
        val br = Point2D(max(p1.x, p2.x), max(p1.y, p2.y))
        val edges = listOf(
            Line(tl, tr),
            Line(tr, br),
            Line(br, bl),
            Line(bl, tl),
        )

        return lines.all {
            doesNotIntersect(it, edges)
        }
    }

    private fun doesNotIntersect(
        line: Line,
        edges: List<Line>
    ): Boolean {
        val minX = edges.minOf { min(it.x1, it.x2) }
        val maxX = edges.maxOf { max(it.x1, it.x2) }
        val minY = edges.minOf { max(it.y1, it.y2) }
        val maxY = edges.maxOf { max(it.y1, it.y2) }

        return max(line.x1, line.x2) <= minX
                || min(line.x1, line.x2) >= maxX
                || max(line.y1, line.y2) <= minY
                || min(line.y1, line.y2) >= maxY
    }
}