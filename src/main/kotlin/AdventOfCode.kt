import lib.Runner
import y2020.Day1

object AoC2020 {
    val days = listOf(Day1)
}

fun main(args: Array<String>) {
    val day = if (args.isEmpty()) 0 else args[0].toInt()
    Runner.run(2020, AoC2020.days, day)
}