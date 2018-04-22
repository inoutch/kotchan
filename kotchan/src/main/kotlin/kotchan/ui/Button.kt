package kotchan.ui

import interop.graphic.GLCamera
import kotchan.controller.TouchType
import kotchan.controller.touchable.RectTouchable
import kotchan.scene.drawable.Sprite
import kotchan.texture.TextureAtlas
import utility.type.Rect
import utility.type.Vector2

class Button(textureAtlas: TextureAtlas,
             private val normalName: String,
             private val pressedName: String,
             camera: GLCamera,
             onClick: () -> Unit) : Sprite(textureAtlas) {

    // this size is applied from normal texture atlas
    private var isBegan = false

    init {
        setAtlas(normalName)
    }

    private fun rect() = Rect(Vector2(position.x, position.y) - anchorPoint * size, size)

    val touchable = RectTouchable({ rect() }, camera) { index, _, type, check ->
        if (index != 0) {
            return@RectTouchable
        }
        if (type == TouchType.Began && check) {
            this.setAtlas(pressedName)
            isBegan = true
        }
        if (type == TouchType.Ended && isBegan) {
            if (check) {
                isBegan = false
                onClick()
            }
            this.setAtlas(normalName)
        }
        if (type == TouchType.Cancelled) {
            isBegan = false
            this.setAtlas(normalName)
        }
    }
}