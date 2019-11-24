package io.github.inoutch.kotchan.math

import io.github.inoutch.kotchan.core.math.Vector4F
import kotlin.test.Test
import kotlin.test.assertEquals

class Vector4FTest {
    @Test
    fun checkPlus() {
        val a = Vector4F(5.3f, 2.6f, 8.4f, 0.9f)
        val b = Vector4F(7.2f, 6.4f, 0.2f, 3.3f)

        assertEquals(Vector4F(5.3f + 7.2f, 2.6f + 6.4f, 8.4f + 0.2f, 0.9f + 3.3f), a + b)

        val c = 5.4f
        assertEquals(Vector4F(5.3f + 5.4f, 2.6f + 5.4f, 8.4f + 5.4f, 0.9f + 5.4f), a + c)
    }

    @Test
    fun checkMinus() {
        val a = Vector4F(5.3f, 2.6f, 8.4f, 0.9f)
        val b = Vector4F(7.2f, 6.4f, 0.2f, 3.3f)

        assertEquals(Vector4F(5.3f - 7.2f, 2.6f - 6.4f, 8.4f - 0.2f, 0.9f - 3.3f), a - b)

        val c = 5.4f
        assertEquals(Vector4F(5.3f - 5.4f, 2.6f - 5.4f, 8.4f - 5.4f, 0.9f - 5.4f), a - c)
    }

    @Test
    fun checkTimes() {
        val a = Vector4F(5.3f, 2.6f, 8.4f, 0.9f)
        val b = Vector4F(7.2f, 6.4f, 0.2f, 3.3f)

        assertEquals(Vector4F(5.3f * 7.2f, 2.6f * 6.4f, 8.4f * 0.2f, 0.9f * 3.3f), a * b)

        val c = 5.4f
        assertEquals(Vector4F(5.3f * 5.4f, 2.6f * 5.4f, 8.4f * 5.4f, 0.9f * 5.4f), a * c)
    }

    @Test
    fun checkDiv() {
        val a = Vector4F(5.3f, 2.6f, 8.4f, 0.9f)
        val b = Vector4F(7.2f, 6.4f, 0.2f, 3.3f)

        assertEquals(Vector4F(5.3f / 7.2f, 2.6f / 6.4f, 8.4f / 0.2f, 0.9f / 3.3f), a / b)

        val c = 5.4f
        assertEquals(Vector4F(5.3f / 5.4f, 2.6f / 5.4f, 8.4f / 5.4f, 0.9f / 5.4f), a / c)
    }
}
