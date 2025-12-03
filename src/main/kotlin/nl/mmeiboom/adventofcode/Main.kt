package nl.mmeiboom.adventofcode

import nl.mmeiboom.adventofcode.lib.Solution
import org.reflections.Reflections
import java.lang.reflect.Constructor
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Runner logic courtesy of C. Tankink,
 * allowing me to play around with reflection and catch up on 10 years of AoC.
 */
fun main() {
    val year = 2015
    val day = 5//getCurrentDay()
    solveDay(day, year) { s ->
        val solver = getConstructorOfDay(day, year).newInstance(s)
        solver
    }
}

private fun getCurrentDay(override: Int? = null): Int {
    return override ?: LocalDateTime.now().dayOfMonth
}

private fun getConstructorOfDay(day: Int, year: Int): Constructor<out Solution<*, *>> {
    val reflections = Reflections("nl.mmeiboom.adventofcode.y$year.day$day")
    return reflections.getSubTypesOf(Solution::class.java).first().getConstructor(String::class.java)
}

private fun <I, S> solveDay(day: Int, year: Int, constructor: (String) -> Solution<I, S>) {
    val dayPrefix = "/nl/mmeiboom/adventofcode/y$year/day$day/"
    constructor("${dayPrefix}sample1")
    constructor("${dayPrefix}sample2")
    val input = constructor("${dayPrefix}input.txt")

    runSolution("Input star 1: ") { input.star1() }
    runSolution("Input star 2: ") { input.star2() }
}

@OptIn(ExperimentalTime::class)
private fun <S> runSolution(message: String, function: () -> S) {
    var solution: S
    val time = measureTime {
        solution = function()
    }

    println("$message$solution")
    println("Time: ${time.inWholeMilliseconds}ms (${time.inWholeMicroseconds}µs)")
}