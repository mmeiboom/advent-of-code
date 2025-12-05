package nl.mmeiboom.adventofcode.lib

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.collections.getOrElse

@Suppress("UNCHECKED_CAST")
data class Matrix<T>(
    val points: Map<Point2D, T>
) {
    fun toMutableMap(): MutableMap<Point2D, T> {
        return points.toMutableMap()
    }

    fun print() {
        val min = points.keys.min()
        val max = points.keys.max()
        for (y in min.y..max.y) {
            for (x in min.x..max.x) {
                print(points.getOrElse(Point2D(x, y), { ' ' }))
            }
            println()
        }
    }

    fun writeToFile(fileName: String, rgbFunction: (T) -> Int) {
        val min = Point2D(0, 0)
        val max = points.keys.max()
        val width = max.x + 1
        val height = max.y + 1

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        for (y in min.y..max.y) {
            for (x in min.x..max.x) {
                val v = (points[Point2D(x, y)] ?: 0) as T
                val rgb = rgbFunction(v)
                val color = Color(rgb,rgb,rgb)
                image.setRGB(x, y, color.rgb)
            }
        }

        ImageIO.write(image, "png", File(fileName))
    }

    companion object {

        fun from(input: List<String>) = from(input, { it })

        fun <T> from(input: List<String>, fn: (char: Char) -> T): Matrix<T> {
            val map: MutableMap<Point2D, T> = mutableMapOf()
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    map[Point2D(x, y)] = fn(char)
                }
            }
            return Matrix(map)
        }
    }
}
