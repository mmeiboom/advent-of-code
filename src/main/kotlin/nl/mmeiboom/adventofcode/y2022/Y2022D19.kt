package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import java.util.*

object Y2022D19 : Day {

    private val blueprints = resourceLines(2022, 19).parse()

    override fun part1(): Any {
        return blueprints.sumOf {
            val qualityScore = calculateMaxGeodes(blueprint = it, time = 24) * it.id
            println("Rated blueprint ${it.id} with a score of $qualityScore")
            qualityScore
        }
    }

    private fun calculateMaxGeodes(blueprint: Blueprint, time: Int): Int {
        var maxGeodes = 0
        val queue = PriorityQueue<Inventory>()
        queue.add(Inventory(oreBots = 1, timeSpent = 0))

        while (queue.isNotEmpty()) {
            val inventory = queue.poll()
            if (inventory.canLeadToBetterResult(maxGeodes, time)) {
                queue.addAll(inventory.calculateBuildOptions(blueprint, time))
            }
            maxGeodes = maxOf(maxGeodes, inventory.materials.geodes)
        }

        return maxGeodes
    }

    override fun part2(): Any {
        val geodes = blueprints.take(3).map {
            val geodes = calculateMaxGeodes(blueprint = it, time = 32)
            println("Blueprint ${it.id} can produce $geodes geodes")
            geodes
        }
        return geodes[0] * geodes[1] * geodes[2]
    }

    data class Inventory(
        val oreBots: Int = 0,
        val clayBots: Int = 0,
        val obsidianBots: Int = 0,
        val geodeBots: Int = 0,
        val materials: Materials = Materials(),
        val timeSpent: Int = 1
    ) : Comparable<Inventory> {

        override fun compareTo(other: Inventory): Int = other.materials.geodes.compareTo(materials.geodes)

        fun canLeadToBetterResult(maxGeodes: Int, totalTime: Int): Boolean {
            val timeLeft = totalTime - timeSpent
            val potential = IntRange(0, timeLeft - 1).sumOf { it + geodeBots }
            return materials.geodes + potential > maxGeodes
        }

        /**
         * Determine valid choices at this point
         *
         * 1. Prioritize building geode bots over any other action
         * 2. Don't build more harvesting bots than you can spend in a turn on building
         * 3. Don't build if a bot costs more than it will ever harvest
         * ?. Always work towards a build (can take multiple turns)
         */
        fun calculateBuildOptions(blueprint: Blueprint, timeBudget: Int): Collection<Inventory> {
            val options = mutableListOf<Inventory>()
            val timeRemaining = timeBudget - timeSpent

            if(timeRemaining == 0) {
                return options
            }

            val canBuildGeodeBot = materials.contains(blueprint.geodeBot)
            val canBuildObsidianBot = materials.contains(blueprint.obsidianBot)
            val canBuildOreBot = materials.contains(blueprint.oreBot)
            val canBuildClayBot = materials.contains(blueprint.clayBot)

            // Heuristics
            val shouldBuildMoreObsidianBots = obsidianBots < blueprint.allConstructionCost().maxOf { it.obsidian }
            val shouldBuildMoreClayBots = clayBots < blueprint.allConstructionCost().maxOf { it.clay }
            val shouldBuildMoreOreBots =
                oreBots < blueprint.allConstructionCost().maxOf { it.ore }
                        && blueprint.oreBot.ore < timeRemaining


            if (canBuildGeodeBot) {
                options.add(blueprint.geodeBot.build(this))
            } else {
                if (canBuildObsidianBot && shouldBuildMoreObsidianBots) {
                    options.add(blueprint.obsidianBot.build(this))
                }
                if (canBuildOreBot && shouldBuildMoreOreBots) {
                    options.add(blueprint.oreBot.build(this))
                }
                if (canBuildClayBot && shouldBuildMoreClayBots) {
                    options.add(blueprint.clayBot.build(this))
                }

                // Route when we choose to not build
                options.add(Materials().build(this))
            }

            return options
        }
    }

    data class Materials(
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geodes: Int = 0,
        val oreBotsToBuild: Int = 0,
        val clayBotsToBuild: Int = 0,
        val obsidianBotsToBuild: Int = 0,
        val geodeBotsToBuild: Int = 0,
    ) {
        fun contains(requiredMaterials: Materials) =
            this.ore >= requiredMaterials.ore
                    && this.clay >= requiredMaterials.clay
                    && this.obsidian >= requiredMaterials.obsidian
                    && this.geodes >= requiredMaterials.geodes

        fun build(inventory: Inventory): Inventory {
            return inventory.copy(
                timeSpent = inventory.timeSpent + 1,
                materials = Materials(
                    ore = inventory.materials.ore - ore + inventory.oreBots,
                    clay = inventory.materials.clay - clay + inventory.clayBots,
                    obsidian = inventory.materials.obsidian - obsidian + inventory.obsidianBots,
                    geodes = inventory.materials.geodes + inventory.geodeBots
                ),
                oreBots = inventory.oreBots + oreBotsToBuild,
                clayBots = inventory.clayBots + clayBotsToBuild,
                obsidianBots = inventory.obsidianBots + obsidianBotsToBuild,
                geodeBots = inventory.geodeBots + geodeBotsToBuild
            )
        }

    }

    data class Blueprint(
        val id: Int,
        val oreBot: Materials,
        val clayBot: Materials,
        val obsidianBot: Materials,
        val geodeBot: Materials
    ) {
        fun allConstructionCost() = listOf(oreBot, clayBot, obsidianBot, geodeBot)
    }

    private fun List<String>.parse(): List<Blueprint> {
        val regex =
            "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.".toRegex()
        return this.map { line ->
            val matches = regex.find(line)!!.groupValues
            val values = matches.subList(1, matches.size).map { it.toInt() }
            Blueprint(
                id = values[0],
                oreBot = Materials(ore = values[1], oreBotsToBuild = 1),
                clayBot = Materials(ore = values[2], clayBotsToBuild = 1),
                obsidianBot = Materials(ore = values[3], clay = values[4], obsidianBotsToBuild = 1),
                geodeBot = Materials(ore = values[5], obsidian = values[6], geodeBotsToBuild = 1),
            )
        }
    }
}

fun main() {
//    val p1 = Y2022D19.part1()
//    println("P1: $p1")
    val p2 = Y2022D19.part2()
    println("P2: $p2")

}