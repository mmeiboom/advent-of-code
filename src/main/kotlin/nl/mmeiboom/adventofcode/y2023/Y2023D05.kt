package nl.mmeiboom.adventofcode.y2023

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.y2023.Y2023D05.Category.*

object Y2023D05 : Day {

    val input = resourceLines(2023, 5)
    val seeds = Regex("[0-9]+").findAll(input.first())
        .map { it.value.toLong() }
        .toList()

    val maps = input.drop(1).parse()


    enum class Category {
        SEED, SOIL, FERTILIZER, WATER, LIGHT, TEMPERATURE, HUMIDITY, LOCATION
    }

    data class Converter(val mappings: List<ConversionRange>) {
        fun convert(value: Long): Long {
            return mappings.firstOrNull { it.contains(value) }?.convert(value) ?: value
        }
    }

    data class ConversionRange(val start: Long, val destination: Long, val length: Long) {
        fun contains(value: Long): Boolean {
            return value >= start && value < start + length
        }
        fun convert(value: Long): Long {
            val offset = destination - start
            return value + offset
        }
    }

    data class SeedRange(val from: Long, val length: Long)

    override fun part1() = seeds.minOf { convertSeed(it) }

    override fun part2(): Long {
        val seedRanges = mutableListOf<SeedRange>()
        for(i in seeds.indices step 2) {
            seedRanges.add(SeedRange(seeds[i], seeds[i+1]))
        }

        return seedRanges.minOf { minForRange(it) }
    }

    private fun minForRange(range: SeedRange) : Long {
        var min = Long.MAX_VALUE
        for(i in 0 until range.length) {
            min = Math.min(min, convertSeed(range.from + i))
        }
        return min
    }

    private fun convertSeed(number: Long) : Long {
        return convert(convert(convert(convert(convert(convert(convert(number, SEED, SOIL), SOIL, FERTILIZER), FERTILIZER, WATER), WATER, LIGHT), LIGHT, TEMPERATURE), TEMPERATURE, HUMIDITY), HUMIDITY, LOCATION)
    }

    private fun convert(value: Long, from: Category, to: Category): Long {
        return maps.getValue(from).convert(value)
    }

    private fun List<String>.parse(): Map<Category, Converter> {
        val mappings = mutableMapOf<Category, Converter>()
        val conversions = mutableListOf<ConversionRange>()
        val headerRegex = Regex("(\\w+)-to-(\\w+) map:")
        val conversionRegex = Regex("(\\d+) (\\d+) (\\d+)")
        var currentCategory : Category? = null

        this.forEach {line ->
            if(headerRegex.matches(line)) {
                val matches = headerRegex.findAll(line)
                if(currentCategory != null && conversions.isNotEmpty()) {
                    mappings[currentCategory!!] = Converter(conversions.toList())
                }

                currentCategory = Category.valueOf(matches.first().groupValues[1].uppercase())
                conversions.clear()
            } else if(conversionRegex.matches(line)) {
                val matches = conversionRegex.findAll(line)
                val destinationStart = matches.first().groupValues[1].toLong()
                val sourceStart = matches.first().groupValues[2].toLong()
                val length = matches.first().groupValues[3].toLong()
                conversions.add(ConversionRange(sourceStart, destinationStart, length))
            }
        }

        if(currentCategory != null && conversions.isNotEmpty()) {
            mappings[currentCategory!!] = Converter(conversions.toList())
            conversions.clear()
        }

        return mappings.toMap()
    }
}

fun main() {
    print(Y2023D05.part2())
}