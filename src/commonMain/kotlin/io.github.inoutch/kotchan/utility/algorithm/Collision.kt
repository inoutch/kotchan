package io.github.inoutch.kotchan.utility.algorithm

import io.github.inoutch.kotchan.utility.type.Vector2

class Collision {
    companion object {
        fun checkPointAndPolygon(p: Vector2, a: Vector2, b: Vector2, c: Vector2): Boolean {
            val ab = b - a
            val bp = p - b
            val bc = c - b
            val cp = p - c
            val ca = a - c
            val ap = p - a

            val c1 = ab.x * bp.y - ab.y * bp.x
            val c2 = bc.x * cp.y - bc.y * cp.x
            val c3 = ca.x * ap.y - ca.y * ap.x

            return (c1 > 0 && c2 > 0 && c3 > 0) || (c1 < 0 && c2 < 0 && c3 < 0)
        }
    }
}
