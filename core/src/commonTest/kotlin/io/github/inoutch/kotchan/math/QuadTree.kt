package io.github.inoutch.kotchan.math

import io.github.inoutch.kotchan.extension.toCombination
import io.github.inoutch.kotchan.utility.Timer
import io.github.inoutch.kotchan.utility.measure
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class QuadTreeTest {
    // Manual test: https://www.desmos.com/calculator

    @Test
    fun checkMortonNumber() {
        assertEquals(50, QuadTree.calcMortonNumber(Vector2I(4, 5)))
        assertEquals(45, QuadTree.calcMortonNumber(Vector2I(3, 6)))
    }

    @Test
    fun checkCellLength() {
        assertEquals(1, QuadTree.calcCellPosition(1))
        assertEquals(5, QuadTree.calcCellPosition(2))
        assertEquals(21, QuadTree.calcCellPosition(3))
    }

    @Test
    fun checkCellShift() {
        val offsets = List(4) { QuadTree.calcCellPosition(it) }
        assertEquals(1 + 1, QuadTree.calcMortonNumber(Vector2I(5, 1), Vector2I(7, 3), 4, offsets))
        assertEquals(11 + 5, QuadTree.calcMortonNumber(Vector2I(2, 6), Vector2I(3, 7), 4, offsets))
        assertEquals(0, QuadTree.calcMortonNumber(Vector2I(3, 3), Vector2I(6, 6), 4, offsets))
    }

    @Test
    fun check3Nodes() {
        val quadTree = QuadTree<QuadTree.Object>(3, Vector2F(100.0f, 100.0f))
        // A1
        quadTree.add(object : QuadTree.Object {
            override val position: Vector2F = Vector2F(70.0f, 20.0f)
            override val size: Vector2F = Vector2F(20.0f, 20.0f)
        })
        // A2
        quadTree.add(object : QuadTree.Object {
            override val position: Vector2F = Vector2F(40.0f, 40.0f)
            override val size: Vector2F = Vector2F(50.0f, 50.0f)
        })
        // A3
        quadTree.add(object : QuadTree.Object {
            override val position: Vector2F = Vector2F(28.0f, 28.0f)
            override val size: Vector2F = Vector2F(20.0f, 20.0f)
        })
        var count = 0
        quadTree.collision { obj1, obj2 ->
            count++
        }
        assertEquals(2, count)
    }

    @Test
    fun checkCollision() {
        val tree = QuadTree<QuadTree.Object>(4, Vector2F(800.0f, 800.0f))
        val a1 = object : QuadTree.Object {
            override val position = Vector2F(580.0f, 180.0f)
            override val size = Vector2F(200.0f, 200.0f)
        }
        tree.add(a1)
        val a2 = object : QuadTree.Object {
            override val position = Vector2F(490.0f, 220.0f)
            override val size = Vector2F(200.0f, 200.0f)
        }
        tree.add(a2)

        var count = 0
        tree.collision { obj1, obj2 ->
            count++
            assertEquals(true, Circle.collision(
                    Circle(obj1.position + obj1.size / 2.0f, obj1.size.x / 2.0f),
                    Circle(obj2.position + obj2.size / 2.0f, obj2.size.x / 2.0f)))
        }

        assertEquals(1, count)
    }

    @Test
    fun checkRandom() {
        val tree = QuadTree<QuadTree.Object>(6, Vector2F(800, 800))
        val objects = List(100) {
            val size = Random.nextInt(1, 80) * 10
            val position = Vector2F(
                    Random.nextInt(0, (800 - size) / 10) * 10,
                    Random.nextInt(0, (800 - size) / 10) * 10)
            object : QuadTree.Object {
                override val position = position
                override val size = Vector2F(size, size)
                override fun toString(): String {
                    return "p(${position.x},${position.y}) s($size)\n"
                }
            }.also { tree.add(it) }
        }

        var count = 0
        var n1 = 0
        var n2 = 0
        println("checkRandom")
        Timer.measure { t ->
            objects.toCombination().forEach {
                if (Circle.collision(
                                Circle(it.first.position + it.first.size / 2.0f, it.first.size.x / 2.0f),
                                Circle(it.second.position + it.second.size / 2.0f, it.second.size.x / 2.0f))) {
                    count++
                }
                n1++
            }
            println("time1: ${t.lap() / 1000.0f}")
        }
        Timer.measure { t ->
            tree.collision { obj1, obj2 ->
                if (Circle.collision(
                                Circle(obj1.position + obj1.size / 2.0f, obj1.size.x / 2.0f),
                                Circle(obj2.position + obj2.size / 2.0f, obj2.size.x / 2.0f))) {
                    count--
                }
                n2++
            }
            println("time2: ${t.lap() / 1000.0f}")
        }
        println("diff: $n2 / $n1")
        assertEquals(0, count)
    }

    @Test
    fun checkRandomForSingle() {
        val tree = QuadTree<QuadTree.Object>(6, Vector2F(800, 800))
        val objects = List(100) {
            val size = Random.nextInt(1, 80) * 10
            val position = Vector2F(
                    Random.nextInt(0, (800 - size) / 10) * 10,
                    Random.nextInt(0, (800 - size) / 10) * 10
            )
            object : QuadTree.Object {
                override val position = position
                override val size = Vector2F(size, size)
                override fun toString(): String {
                    return "p(${position.x},${position.y}) s($size)\n"
                }
            }.also { tree.add(it) }
        }

        val size = Random.nextInt(1, 80) * 10
        val position = Vector2F(
                Random.nextInt(0, (800 - size) / 10) * 10,
                Random.nextInt(0, (800 - size) / 10) * 10
        )
        val target = object : QuadTree.Object {
            override val position = position
            override val size = Vector2F(size, size)
            override fun toString(): String {
                return "p(${position.x},${position.y}) s($size)\n"
            }
        }

        var count = 0
        var n1 = 0
        var n2 = 0
        println("checkRandom")
        Timer.measure { t ->
            objects.forEach {
                if (Circle.collision(
                                Circle(target.position + target.size / 2.0f, target.size.x / 2.0f),
                                Circle(it.position + it.size / 2.0f, it.size.x / 2.0f))) {
                    count++
                }
                n1++
            }
            println("time1: ${t.lap() / 1000.0f}")
        }
        Timer.measure { t ->
            tree.collision(target) {
                if (Circle.collision(
                                Circle(target.position + target.size / 2.0f, target.size.x / 2.0f),
                                Circle(it.position + it.size / 2.0f, it.size.x / 2.0f))) {
                    count--
                }
                n2++
            }
            println("time2: ${t.lap() / 1000.0f}")
        }
        println("diff: $n2 / $n1")
    }
}
