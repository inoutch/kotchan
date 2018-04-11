package kotchan.ui

import kotchan.controller.checkOnlyBegan
import kotchan.controller.checkOnlyEnded
import kotchan.controller.touchable.RectTouchable
import kotchan.scene.drawable.Sprite
import kotchan.texture.TextureAtlas
import utility.type.Rect
import utility.type.Vector2

class Button(textureAtlas: TextureAtlas,
             private val normalName: String,
             private val pressedName: String,
             onClick: () -> Boolean) : Sprite(textureAtlas) {
    private val normalFrame = textureAtlas.frame(normalName)
    private val pressedFrame = textureAtlas.frame(pressedName)

    // this size is applied from normal texture atlas
    private val size: Vector2 = normalFrame?.sourceSize ?: Vector2()

    init {
        setAtlas(normalName)
    }

    val touchable = RectTouchable(Rect(Vector2(), size)) { _, type ->
        when {
            type.checkOnlyBegan() -> this.setAtlas(pressedName)
            type.checkOnlyEnded() -> this.setAtlas(normalName)
            else -> this.setAtlas(normalName)
        }
        true
    }
}