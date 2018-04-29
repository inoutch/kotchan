package kotchan.view.ui

import kotchan.view.drawable.Sprite
import kotchan.view.texture.TextureAtlas
import utility.type.Vector2
import utility.type.Vector3
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class Counter(textureAtlas: TextureAtlas, digit: Int) {
    private val numerics = List(digit, { Sprite(textureAtlas) })
    var value: Int = 0
        set(value) {

        }
    var position = Vector2()
        set(value) {
            updatePosition()
            field = value
        }

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