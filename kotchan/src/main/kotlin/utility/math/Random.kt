package utility.math

class Random {
    companion object {
        private var seed = 0
        fun next(): Int {
            seed = (1103515245 * seed + 12345) and 0x7fffffff
            return seed
        }

        fun next(maxExclusiveValue: Int) = if (maxExclusiveValue == 0) 0 else next() % maxExclusiveValue

        fun next(minInclusiveValue: Int, maxInclusiveValue: Int) =
                minInclusiveValue + next(maxInclusiveValue - minInclusiveValue + 1)
    }
}