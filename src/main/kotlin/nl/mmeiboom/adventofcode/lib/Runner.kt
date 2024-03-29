package nl.mmeiboom.adventofcode.lib

import java.time.format.DateTimeFormatter

object Runner {
    private const val RESULT_WIDTH = 33
    private const val TIME_WIDTH = 9
    private const val SINGLE_ITER = 1

    private val dayOfWeek = DateTimeFormatter.ofPattern("EE")
    private val format = "%6s: %${RESULT_WIDTH}s %${RESULT_WIDTH}s %${TIME_WIDTH}s %${TIME_WIDTH}s %${TIME_WIDTH}s"

    fun run(days: List<Day>, day: Int = 0) {
        println(format.format("Day", "Part 1", "Part 2", "Time", "P1", "P2"))
        if (day == 0) {
            days.forEach { run(it) }
        } else {
            if (day < 1 || day > days.size) {
                println("Day can't be less than 1 or larger than ${days.size}")
                return
            }
            (0 until SINGLE_ITER).forEach {
                run(days[day - 1])
            }
        }
    }

    private fun run(day: Day) {
        val dayName = day.javaClass.simpleName.takeLast(2).toInt()
        val start1 = System.currentTimeMillis()
        val p1 = try {
            day.part1()
        } catch (e: NotImplementedError) {
            "-"
        }
        val dur1 = System.currentTimeMillis() - start1
        val start2 = System.currentTimeMillis()
        val p2 = try {
            day.part2()
        } catch (e: NotImplementedError) {
            "-"
        }
        val dur2 = System.currentTimeMillis() - start2

        println(
            format.format(
                "$dayName ",
                p1,
                p2,
                formatDuration(System.currentTimeMillis() - start1),
                formatDuration(dur1),
                (formatDuration(dur2))
            )
        )
    }
}