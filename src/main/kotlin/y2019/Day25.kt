import lib.Day
import lib.IntCodeComputer
import lib.permute
import lib.resourceString

object Day25 : Day {

    private val program = resourceString(2019, 25).split(",").map { it.toLong() }

    override fun part1(): Any {
        val droid = Droid(program)
        droid.findPassword()

        return "?"
    }

    override fun part2(): Any {
        return "?"
    }

    data class Droid(val program: List<Long>) {
        private val computer = IntCodeComputer(program)

        fun findPassword() {
            // step 1
            val collectAllAndMoveNearSensor = mutableListOf(
                    "north", "take polygon",
                    "north", "take astrolabe",
                    "south", "south",
                    "west", "take hologram",
                    "north",
                    "east", "take space law space brochure",
                    "west",
                    "north", "take prime number",
                    "south", "south", "east", "south",
                    "east", "take weather machine",
                    "west",
                    "south", "take manifold",
                    "west", "take mouse",
                    "west", "north", "north",
                    "inv"
            )

            while (true) {
                val output = computer.readLine()
                println("Droid: $output")
                if (output == "Command?") {
                    if (collectAllAndMoveNearSensor.isNotEmpty()) {
                        val command = collectAllAndMoveNearSensor.removeAt(0)
                        println("$> $command")
                        computer.writeLine(command)
                    } else {
//                        val input = readLine()!!
//                        print("$> ")
//                        computer.writeLine(input)
                        attemptToPassSecurity()
                        return
                    }
                }
            }
        }


        /*****
         *
         *
         *
         *                       NAV --- CREW            Corridor
         *                                 |                |
         *                                WDM --- ENG    Kitchen --- Observatory
         *                                 |                |
         *          PSG --- HCF --- Giftwrap Center --- Hull Breach
         *                   |                              |
         *                Stables                        Storage --- Science Lab
         *                                                  |
         *                                SCP -- Sensor     |
         *                                 |                |
         *                              Hallway             |
         *                                 |                |
         *                             Sick bay  -------  Arcade
         *                                                  |
         *                                               Holodeck
         *
         * val baseSet = listOf("polygon", "astrolabe", "hologram", "space law space brochure", "mouse", "manifold", "prime number", "weather machine")
         *
         */


        private fun executeAndWaitForNextCommand(command: String) {
            runWhileOutput()
            println("$> $command")
            computer.writeLine(command)
            while(true) {
                val output = computer.readLine()
                println(output)
                if (output == "Command?") {
                    return
                }
            }
        }

        private fun runWhileOutput() {
            var output = ""
            while (computer.output.isNotEmpty() && output != "Command?") {
                output = computer.readLine()
                println(output)
            }
        }

        private fun attemptToPassSecurity() {
            val baseSet = listOf("polygon", "astrolabe", "hologram", "space law space brochure", "mouse", "manifold", "prime number", "weather machine")
            val powerSet = PowerSet(baseSet).powerset

            powerSet.forEach { set ->
                baseSet.forEach { executeAndWaitForNextCommand("drop $it") }
                set.forEach { executeAndWaitForNextCommand("take $it") }
                executeAndWaitForNextCommand("inv")

                val passedSecurity = trySensor("east")
                if(passedSecurity) {
                    println("Passed security gate with $set")
                } else {
                    println("Failed security gate with $set")
                }
            }
        }

        private fun trySensor(direction: String) : Boolean {
            println("$> $direction")
            computer.writeLine("$direction")
            while(true) {
                val output = computer.readLine()
                println(output)
                if(output.startsWith("A loud, robotic voice says")) {
                    val passed = !output.contains("Alert!")
                    executeAndWaitForNextCommand("inv")
                    return passed
                }
            }
        }

    }

    class PowerSet<T>(val items: List<T>) {
        private lateinit var combination: IntArray
        val powerset : MutableList<List<T>> = mutableListOf()

        init {
            for (m in 0..items.size) {
                combination = IntArray(m)
                generate(0, m)
            }
        }

        private fun generate(k: Int, m: Int) {
            if (k >= m) {
                powerset.add(combination.map { items[it] })
            }
            else {
                for (j in 0 until items.size)
                    if (k == 0 || j > combination[k - 1]) {
                        combination[k] = j
                        generate(k + 1, m)
                    }
            }
        }
    }

}

fun main() {
    println(Day25.part1())
    println(Day25.part2())
}