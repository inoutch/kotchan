package io.github.inoutch.kotchan.math

import kotlin.test.Test
import kotlin.test.assertEquals

class Vector3FTest {
    @Test
    fun checkPlus() {
        val a = Vector3F(0.2f, 4.8f, 7.3f)
        val b = Vector3F(6.8f, 4.4f, 8.4f)

        assertEquals(Vector3F(0.2f + 6.8f, 4.8f + 4.4f, 7.3f + 8.4f), a + b)
    }

    @Test
    fun checkMinus() {
        val a = Vector3F(0.2f, 4.8f, 7.3f)
        val b = Vector3F(6.8f, 4.4f, 8.4f)

        assertEquals(Vector3F(0.2f - 6.8f, 4.8f - 4.4f, 7.3f - 8.4f), a - b)
    }

    @Test
    fun checkTimes() {
        val a = Vector3F(0.2f, 4.8f, 7.3f)
        val b = Vector3F(6.8f, 4.4f, 8.4f)

        assertEquals(Vector3F(0.2f * 6.8f, 4.8f * 4.4f, 7.3f * 8.4f), a * b)

        val c = 5.9f
        assertEquals(Vector3F(c * 0.2f, c * 4.8f, c * 7.3f), a * c)
    }

    @Test
    fun checkDiv() {
        val a = Vector3F(0.2f, 4.8f, 7.3f)
        val b = Vector3F(6.8f, 4.4f, 8.4f)

        assertEquals(Vector3F(0.2f / 6.8f, 4.8f / 4.4f, 7.3f / 8.4f), a / b)

        val c = 5.9f
        assertEquals(Vector3F(0.2f / c, 4.8f / c, 7.3f / c), a / c)
    }

    @Test
    fun checkCrossProduct() {
        val a = Vector3F(1.0f, 2.0f, 0.0f)
        val b = Vector3F(0.0f, 1.0f, -1.0f)

        assertEquals(Vector3F(-2.0f, 1.0f, 1.0f), a.crossProduct(b))
    }
}
