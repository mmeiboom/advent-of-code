package nl.mmeiboom.adventofcode.y2025.day2

import nl.mmeiboom.adventofcode.lib.Solution

class GiftShop(fileName: String?) : Solution<List<ProductIdRange>, Long>(fileName) {
    override fun parse(line: String): List<ProductIdRange> {
        return line.split(",").map{
            val (from, to) = it.split("-")
            ProductIdRange(from.toLong(), to.toLong())
        }
    }

    override fun solve1(data: List<List<ProductIdRange>>): Long {
        val ranges = data.single()
        return ranges.flatMap{it.invalidIds()}.sum()
    }

    override fun solve2(data: List<List<ProductIdRange>>): Long {
        val ranges = data.single()
        return ranges.flatMap{it.invalidRepeat()}.sum()
    }

}

data class ProductIdRange(val from:Long, val to:Long) {
    fun invalidIds():List<Long>{
        return from.rangeTo(to).filter {
            isInvalid(it)
        }
    }

    fun invalidRepeat():List<Long>{
        return from.rangeTo(to).filter {
            isInvalidRepeat(it)
        }
    }

    private fun isInvalid(it: Long): Boolean {
        val stringVal = it.toString()
        if(stringVal.length % 2 == 1) return false
        val left = stringVal.take(stringVal.length / 2)
        val right = stringVal.takeLast(stringVal.length / 2)
        return left == right
    }

    private fun isInvalidRepeat(it: Long): Boolean {
        val stringVal = it.toString()
        for(i in 1..stringVal.length/2) {
            for(j in 2..stringVal.length) {
                if(stringVal.take(i).repeat(j) == stringVal) {
                    return true
                }
            }
        }
        return false
    }
}