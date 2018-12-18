package io.github.inoutch.kotchan.core.view.ui

import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.core.view.drawable.Sprite
import io.github.inoutch.kotchan.core.view.texture.TextureAtlas
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class Counter(textureAtlas: TextureAtlas, digit: Int) {

    var value: Int = 0

    var position = Vector2()
        set(value) {
            updatePosition()
            field = value
        }

    private val numerics = List(digit) { Sprite(textureAtlas) }

    private fun getDigit(value: Int) = floor(log10(value.toFloat())).toInt() + 1

    private fun updatePosition() {
        val digit = getDigit(value)
        val n10 = 10.0f
        numerics.forEachIndexed { index, sprite ->
            if (index < digit) {
                val n = (value / n10.pow(index)).toInt() % 10
                sprite.visible = true
                sprite.setAtlas(n)
                sprite.position = Vector3()
            } else {
                sprite.visible = false
            }
        }
    }
}