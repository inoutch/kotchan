package io.github.inoutch.kotchan.utility.type

class Color {
    companion object {
        fun hsv2rgb(h: Float, s: Float, v: Float): Vector3 {
            var r = v
            var g = v
            var b = v
            val hh = h * 6.0f

            if (s > 0.0f) {
                val i = hh.toInt()
                val f = hh - i
                when (i) {
                    0 -> {
                        g *= 1 - s * (1 - f)
                        b *= 1 - s
                    }
                    1 -> {
                        r *= 1 - s * f
                        b *= 1 - s
                    }
                    2 -> {
                        r *= 1 - s
                        b *= 1 - s * (1 - f)
                    }
                    3 -> {
                        r *= 1 - s
                        g *= 1 - s * f
                    }
                    4 -> {
                        r *= 1 - s * (1 - f)
                        g *= 1 - s
                    }
                    5 -> {
                        g *= 1 - s
                        b *= 1 - s * f
                    }
                }
                return Vector3(r, g, b)
            }
            return Vector3(v, v, v)
        }
    }
}