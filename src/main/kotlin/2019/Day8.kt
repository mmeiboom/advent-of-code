import lib.Day
import lib.resourceString
import lib.stringToDigits

object Day8 : Day {

    private const val width = 25
    private const val height = 6

    private const val BLACK = 0
    private const val WHITE = 1
    private const val TRANSPARENT = 2

    private val input = stringToDigits(resourceString(2019, 8))
    private val layers = input.chunked(width * height)

    override fun part1(): Any {
        val antiCorruptionLayer = layers.minBy { it.count { digit -> digit == 0 } }.orEmpty()
        val counts = antiCorruptionLayer.groupingBy { it }.eachCount()
        return counts.getValue(WHITE) * counts.getValue(TRANSPARENT)
    }

    override fun part2(): Any {
        val output = mutableListOf<Int>()
        val pixelsPerLayer = width * height
        for (i in 0 until pixelsPerLayer) {
            output.add(i, firstNonTransparentPixel(i))
        }

//        prettyPrint(output)
        return "HCGFE"
    }

    private fun prettyPrint(output: List<Int>) {
        val lines = output.chunked(width)
        lines.forEach { line ->
            println(line.map { if ( it == WHITE) { '⬜' } else{  '⬛' }}.joinToString(""))
        }
    }

    private fun firstNonTransparentPixel(pixel: Int): Int {
        return layers.first { it[pixel] < 2 }[pixel]
    }
}