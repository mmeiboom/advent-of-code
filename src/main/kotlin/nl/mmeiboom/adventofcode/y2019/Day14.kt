import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.ceil

object Day14 : Day {

    private val recipes = extractRecipes(resourceLines(2019, 14))

    data class Order(val ingredient: String, val quantity: Long)

    data class Recipe(val ingredient: String, val ingredients: Map<String, Long>, val quantity: Long)

    override fun part1() = oresToProduce(1L)

    private fun oresToProduce(quantity: Long): Long {
        val orders = mutableListOf<Order>()
        val stock = mutableMapOf<String, Long>()
        var oreCount = 0L

        // Initialize
        orders.add(Order("FUEL", quantity))

        // Produce while work remains
        while (orders.isNotEmpty()) {
            val order = orders.removeAt(0)
            val quantityInStock = stock.getOrDefault(order.ingredient, 0)

            if (order.ingredient == "ORE") {
                oreCount += order.quantity
            } else if (order.quantity <= quantityInStock) {
                stock[order.ingredient] = quantityInStock - order.quantity
            } else {
                val amountToProduce = order.quantity - quantityInStock
                val recipe = recipes.getValue(order.ingredient)
                val batches = ceil(amountToProduce / recipe.quantity.toDouble()).toInt()

                recipe.ingredients.forEach { (k, v) ->
                    orders.add(Order(ingredient = k, quantity = v * batches))
                }

                val remainder = batches * recipe.quantity - amountToProduce
                stock[order.ingredient] = remainder
            }
        }

        return oreCount
    }

    private fun extractRecipes(resourceLines: List<String>): Map<String, Recipe> {
        val recipes = resourceLines.map {
            val (left, right) = it.split("=>")
            val part = "(\\d+) (\\w+)".toRegex()
            val ingredients = part.findAll(left)
            val result = part.find(right)

            val map = mutableMapOf<String, Long>()
            ingredients.forEach { f ->
                map[f.groupValues[2]] = f.groupValues[1].toLong()
            }

            Recipe(
                    ingredients = map,
                    ingredient = result!!.groupValues[2],
                    quantity = result.groupValues[1].toLong()
            )
        }
        return recipes.map { it.ingredient to it }.toMap()
    }

    override fun part2(): Any {
        val trillion = 1_000_000_000_000L

        var min = 1L
        var max = 1_000_000_000L
        var guess = 0L

        while(max - min > 1) {
            guess = (min + max) / 2
            val ores = oresToProduce(guess)
            if (ores > trillion) {
                max = guess
            } else {
                min = guess
            }
        }

        return guess
    }

}

fun main() {
    println(Day14.part1())
    println(Day14.part2())
}