package io.github.inoutch.kotchan.core.graphic.polygon

import kotlin.test.Test
import kotlin.test.assertEquals

class PartialChangeManagerTest {
    @Test
    fun standard() {
        val partialChangeManager = PartialChangeManager<Int>()

        partialChangeManager.add(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8), 0)

        partialChangeManager.add(listOf(13, 14, 15), 3)

        assertEquals(1, partialChangeManager.blocks().size)

        assertEquals(0, partialChangeManager.blocks().first().offset)

        assertEquals(listOf(0, 1, 2, 13, 14, 15, 6, 7, 8), partialChangeManager.blocks().first().value)

        partialChangeManager.add(listOf(10, 11, 12), 10)

        assertEquals(2, partialChangeManager.blocks().size)
    }

    @Test
    fun override() {
        val partialChangeManager = PartialChangeManager<Int>()

        partialChangeManager.add(listOf(0, 1, 2), 10)

        partialChangeManager.add(listOf(15, 16, 17), 15)

        partialChangeManager.add(listOf(21, 22, 23, 24, 25, 26), 11)

        partialChangeManager.add(listOf(19), 19)

        val blocks = partialChangeManager.blocks()

        assertEquals(2, blocks.size)

        assertEquals(10, blocks.first().offset)

        assertEquals(listOf(0, 21, 22, 23, 24, 25, 26, 17), blocks.first().value)
    }

    @Test
    fun clear() {
        val partialChangeManager = PartialChangeManager<Int>()

        partialChangeManager.add(listOf(1, 2, 3, 4, 5, 6, 7, 8), 0)

        partialChangeManager.add(listOf(13, 14, 15), 2)

        partialChangeManager.clear()

        assertEquals(emptyList(), partialChangeManager.blocks())
    }
}
