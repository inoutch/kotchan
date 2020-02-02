package io.github.inoutch.kotchan.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DisposerTest {
    @Test
    fun checkToDisposeAll() {
        val disposer = Disposer()
        val counter = mutableListOf<Int>()

        disposer.add { counter.add(0) }
        disposer.add { counter.add(3) }
        disposer.add { counter.add(4) }
        disposer.add { counter.add(6) }

        assertEquals(mutableListOf(), counter)
        assertFalse(disposer.isDisposed())

        disposer.dispose()
        assertEquals(mutableListOf(6, 4, 3, 0), counter)
        assertTrue(disposer.isDisposed())
    }

    @Test
    fun checkToDisposeTreeAll() {
        val counter = mutableListOf<String>()
        val nodes = createTree { counter.add(it) }

        assertEquals(mutableListOf(), counter)
        nodes.first().dispose()
        assertEquals(mutableListOf("j", "i", "h", "g", "f", "e", "d", "c", "b", "a"), counter)
    }

    @Test
    fun checkToDisposeTreePartial() {
        val counter = mutableListOf<String>()
        val nodes = createTree { counter.add(it) }

        assertEquals(mutableListOf(), counter)
        nodes[3].dispose()
        nodes[5].dispose()
        nodes[4].dispose()
        nodes[1].dispose()
        nodes[0].dispose()

        assertEquals(mutableListOf("d", "g", "f", "h", "e", "b", "j", "i", "c", "a"), counter)
        nodes.forEach { assertTrue(it.isDisposed()) }
    }

    private fun createTree(callback: (value: String) -> Unit): List<Disposer> {
        /*
            A
            ├ B
            ├ C
            │ └ D
            ├ E
            │ ├ F
            │ │ └ G
            │ └ H
            └ I
            　 └ J
         */
        val a = Disposer().also { it.add { callback("a") } }
        val b = Disposer().also { it.add { callback("b") } }
        val c = Disposer().also { it.add { callback("c") } }
        val d = Disposer().also { it.add { callback("d") } }
        val e = Disposer().also { it.add { callback("e") } }
        val f = Disposer().also { it.add { callback("f") } }
        val g = Disposer().also { it.add { callback("g") } }
        val h = Disposer().also { it.add { callback("h") } }
        val i = Disposer().also { it.add { callback("i") } }
        val j = Disposer().also { it.add { callback("j") } }

        a.add(b)
        a.add(c)
        c.add(d)
        a.add(e)
        e.add(f)
        f.add(g)
        e.add(h)
        a.add(i)
        i.add(j)
        return listOf(a, b, c, d, e, f, g, h, i)
    }
}
