package nl.mmeiboom.adventofcode.lib

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

data class Rectangle(val p1: Point2D, val p2: Point2D) {
    fun edges() : List<Line> {
        val tl = Point2D(min(p1.x, p2.x), min(p1.y, p2.y))
        val tr = Point2D(max(p1.x, p2.x), min(p1.y, p2.y))
        val bl = Point2D(min(p1.x, p2.x), max(p1.y, p2.y))
        val br = Point2D(max(p1.x, p2.x), max(p1.y, p2.y))
        return listOf(
            Line(tl, tr),
            Line(tr, br),
            Line(br, bl),
            Line(bl, tl),
        )
    }

    fun doesNotIntersect(line: Line): Boolean {
        val edges = edges()
        val minX = edges.minOf { min(it.x1, it.x2) }
        val maxX = edges.maxOf { max(it.x1, it.x2) }
        val minY = edges.minOf { min(it.y1, it.y2) }
        val maxY = edges.maxOf { max(it.y1, it.y2) }

        return max(line.x1, line.x2) <= minX
                || min(line.x1, line.x2) >= maxX
                || max(line.y1, line.y2) <= minY
                || min(line.y1, line.y2) >= maxY
    }

    fun surface(): Long {
        val x = (p1.x - p2.x).absoluteValue + 1
        val y = (p1.y - p2.y).absoluteValue + 1
        return x.toLong() * y.toLong()
    }
}
