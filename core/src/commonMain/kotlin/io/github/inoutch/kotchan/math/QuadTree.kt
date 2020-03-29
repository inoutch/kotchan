package io.github.inoutch.kotchan.math

import kotlin.math.pow

class QuadTree<T : QuadTree.Object>(val level: Int, val size: Vector2F) {
    interface Object {
        val position: Vector2F
        val size: Vector2F
    }

    data class Cell<T>(val index: Int, val obj: T)

    data class MortonNumber(val index: Int, val depth: Int)

    companion object {
        fun separateBit32(n: Int): Int {
            var m = n
            m = (m or (m shl 8)) and 0x00ff00ff
            m = (m or (m shl 4)) and 0x0f0f0f0f
            m = (m or (m shl 2)) and 0x33333333
            return (m or (m shl 1)) and 0x55555555
        }

        fun calcMortonNumber(point: Vector2I): Int {
            return separateBit32(point.x) or (separateBit32(point.y) shl 1)
        }

        fun calcCellPosition(level: Int): Int {
            return (4.0f.pow(level).toInt() - 1) / 3
        }

        fun calcCellWidth(level: Int): Int {
            return (2.0f.pow(level - 1).toInt())
        }

        fun calcMortonNumber(p1: Vector2I, p2: Vector2I, level: Int, offsets: List<Int>): Int {
            val a1 = calcMortonNumber(p1)
            val a2 = calcMortonNumber(p2)
            val a3 = calcShift(a1 xor a2, level)
            val offset = offsets[level - a3 - 1]
            return (a2 shr (a3 * 2)) + offset
        }

        fun calcMortonNumber(p1: Vector2I, p2: Vector2I, level: Int): MortonNumber {
            val a1 = calcMortonNumber(p1)
            val a2 = calcMortonNumber(p2)
            val a3 = calcShift(a1 xor a2, level)
            return MortonNumber(a2 shr (a3 * 2), level - a3 - 1)
        }

        fun calcShift(value: Int, maxLevel: Int): Int {
            var i = maxLevel - 2
            while (i >= 0 && (value shr (i * 2)) and 0b11 == 0) {
                i--
            }
            return i + 1
        }
    }

    private val cells = MutableList<MutableList<Cell<T>>>(calcCellPosition(level)) { mutableListOf() }

    private val offsets = List(level) { calcCellPosition(it) }

    private val cellSize = calcCellWidth(level).let { Vector2I(it, it) }

    private val objectIndexes = mutableMapOf<T, Cell<T>>()

    fun add(obj: T) {
        val p1 = obj.position / size * cellSize
        val p2 = p1 + obj.size / size * cellSize
        val index = calcMortonNumber(p1.toVector2I(), p2.toVector2I(), level, offsets)
        if (index < 0 || index >= cells.size) {
            return
        }

        val node = Cell(index, obj)
        cells[index].add(node)
        objectIndexes[obj] = node
    }

    fun remove(obj: T) {
        val node = objectIndexes[obj] ?: return
        cells[node.index].remove(node)
        objectIndexes.remove(obj)
    }

    fun collision(callback: (obj1: T, obj2: T) -> Unit) {
        var morton = 0 // index each spaces
        var depth = 0
        val stack = mutableListOf<T>()
        var cell = cells[morton + offsets[depth]]

        do {
            // Search objects in current cell and stack
            for (i in 0 until cell.size) {
                val currentObj = cell[i].obj
                for (j in i + 1 until cell.size) {
                    callback(currentObj, cell[j].obj)
                }

                for (s in stack) {
                    callback(currentObj, s)
                }
            }

            // Make sure it have the depth
            val offset = offsets.getOrNull(depth + 1)
            if (offset != null) {
                // Add current objects to stack
                for (c in cell) {
                    stack.add(c.obj)
                }
                morton = morton shl 2
                cell = cells[morton + offset]
                depth++
                continue
            }

            var checkedNum = morton and 0b11
            while (true) {
                if (checkedNum < 3) {
                    morton++
                    cell = cells[morton + offsets[depth]]
                    break
                }

                depth--
                morton = (morton shr 2)
                checkedNum = morton and 0b11
                cell = cells[morton + offsets[depth]]
                var i = 0
                val s = cell.size
                while (i < s && stack.isNotEmpty()) {
                    stack.removeAt(stack.lastIndex)
                    i++
                }
            }
        } while (depth != 0)
    }

    fun collision(target: T, callback: (obj: T) -> Unit) {
        val p1 = target.position / size * cellSize
        val p2 = p1 + target.size / size * cellSize
        val m = calcMortonNumber(p1.toVector2I(), p2.toVector2I(), level)

        // Check parent
        var i = m.index
        var d = m.depth
        while (d >= 0) {
            cells[i + offsets[d]].forEach { callback(it.obj) }
            i = i shr 2
            d -= 1
        }

        // Check children
        i = m.index shl 2
        d = m.depth + 1
        var cell = cells[i + offsets[d]]
        do {
            cell.forEach { callback(it.obj) }

            val offset = offsets.getOrNull(d + 1)
            if (offset != null) {
                i = i shl 2
                d += 1
                cell = cells[i + offset]
                continue
            }

            var checkedNum = i and 0x3
            while (true) {
                if (checkedNum < 3) {
                    i++
                    cell = cells[i + offsets[d]]
                    break
                }

                d--
                i = i shr 2
                checkedNum = i and 0x3
            }
        } while (d != m.depth)
    }
}
