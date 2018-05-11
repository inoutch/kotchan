package utility.math

class Random {
    companion object {
        private var seed = 0L
        private var x: Long = 123456789
        private var y: Long = 362436069
        private var z: Long = 521288629
        private var w: Long = 88675123

        fun seed(s: Long) {
            seed = s
            do {
                seed = seed * 1812433253 + 1; seed = seed xor (seed shl 13); seed = seed xor (seed shr 17)
                x = 123464980L xor seed
                seed = seed * 1812433253 + 1; seed = seed xor (seed shl 13); seed = seed xor (seed shr 17)
                y = 3447902351L xor seed
                seed = seed * 1812433253 + 1; seed = seed xor (seed shl 13); seed = seed xor (seed shr 17)
                z = 2859490775 xor seed
                seed = seed * 1812433253 + 1; seed = seed xor (seed shl 13); seed = seed xor (seed shr 17)
                w = 47621719L xor seed
            } while (x == 0L && y == 0L && z == 0L && w == 0L)
        }

        fun next(): Int {
            val t: Long = x.xor(x.shl(11))
            x = y
            y = z
            z = w
            w = w.xor(t.xor(x.ushr(8).xor(w.ushr(19))))
            return w.toInt() and 0x7fffffff
        }

        fun next(maxExclusiveValue: Int) = if (maxExclusiveValue == 0) 0 else next() % maxExclusiveValue

        fun next(minInclusiveValue: Int, maxInclusiveValue: Int) =
                minInclusiveValue + next(maxInclusiveValue - minInclusiveValue + 1)
    }
}