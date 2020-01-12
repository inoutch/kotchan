package io.github.inoutch.kotchan.math

import kotlin.test.Test
import kotlin.test.assertEquals

class Vector2FTest {
    @Test
    fun checkPlus() {
        val a = Vector2F(0.2f, 4.8f)
        val b = Vector2F(6.8f, 4.4f)

        assertEquals(Vector2F(0.2f + 6.8f, 4.8f + 4.4f), a + b)

        val c = Vector2I(1, 2)
        assertEquals(Vector2F(1.toFloat() + 0.2f, 2.toFloat() + 4.8f), c + a)
    }

    @Test
    fun checkMinus() {
        val a = Vector2F(0.2f, 4.8f)
        val b = Vector2F(6.8f, 4.4f)

        assertEquals(Vector2F(0.2f - 6.8f, 4.8f - 4.4f), a - b)

        val c = Vector2I(1, 2)
        assertEquals(Vector2F(1.toFloat() - 0.2f, 2.toFloat() - 4.8f), c - a)
    }

    @Test
    fun checkTimes() {
        val a = Vector2F(0.2f, 4.8f)
        val b = Vector2F(6.8f, 4.4f)

        assertEquals(Vector2F(0.2f * 6.8f, 4.8f * 4.4f), a * b)

        val c = Vector2I(1, 2)
        assertEquals(Vector2F(1.toFloat() * 0.2f, 2.toFloat() * 4.8f), c * a)
    }

    @Test
    fun checkDiv() {
        val a = Vector2F(0.2f, 4.8f)
        val b = Vector2F(6.8f, 4.4f)

        assertEquals(Vector2F(0.2f / 6.8f, 4.8f / 4.4f), a / b)

        val c = Vector2I(1, 2)
        assertEquals(Vector2F(1.toFloat() / 0.2f, 2.toFloat() / 4.8f), c / a)
    }
}
