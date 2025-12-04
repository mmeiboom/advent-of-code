package nl.mmeiboom.adventofcode.lib

data class Line(
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int
    ) {
        fun isHorizontal() = y1 == y2
        fun isVertical() = x1 == x2

        fun pointsOnLine() : List<Point2D> {
            return if(isHorizontal()) {
                range(x1,x2).map { Point2D(it, y1)}
            } else if(isVertical()) {
                range(y1,y2).map { Point2D(x1, it)}
            } else {
                val xs = range(x1, x2)
                val ys = range(y1, y2)
                return xs.zip(ys).map {
                    (x,y) -> Point2D(x,y)
                }
            }
        }
    }

    fun range(from: Int, to:Int) : IntProgression {
        return if (to > from) {
            IntRange(from, to)
        } else {
            IntProgression.fromClosedRange(from, to, -1)
        }
    }
