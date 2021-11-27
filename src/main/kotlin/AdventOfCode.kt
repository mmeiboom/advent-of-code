import lib.Runner
import y2021.*

object AoC2021 {
    val days = listOf(
            Day1, Day2, Day3, Day4, Day5, Day6, Day7, Day8, Day9, Day10,
            Day11, Day12, Day13, Day14, Day15, Day16, Day17, Day18, Day19, Day20,
            Day21, Day22, Day23, Day24, Day25,
    )
}

fun main(args: Array<String>) {
    val day = if (args.isEmpty()) 0 else args[0].toInt()
    Runner.run(2021, AoC2021.days, day)
}
