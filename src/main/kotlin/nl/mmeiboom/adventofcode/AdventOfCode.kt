package nl.mmeiboom.adventofcode

import nl.mmeiboom.adventofcode.lib.Runner
import nl.mmeiboom.adventofcode.lib.Challenges

class AdventOfCode {
    fun run() {
        val day = 0 // 0 = all days, > 0 = specific day
        Runner.run(Challenges.y2023, day)
    }
}

fun main() {
    AdventOfCode().run()
}
