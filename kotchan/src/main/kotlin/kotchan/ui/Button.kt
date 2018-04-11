package kotchan.ui

import interop.graphic.GLCamera
import kotchan.controller.TouchType
import kotchan.controller.likeBegan
import kotchan.controller.likeEnded
import kotchan.controller.touchable.RectTouchable
import kotchan.scene.drawable.Sprite
import kotchan.texture.TextureAtlas
import utility.type.Rect
import utility.type.Vector2

class Button(textureAtlas: TextureAtlas,
             private val normalName: String,
             private val pressedName: String,
             camera: GLCamera,
             onClick: () -> Boolean) : Sprite(textureAtlas) {

    // this size is applied from normal texture atlas
    private val size: Vector2 = textureAtlas.frame(normalName)?.sourceSize ?: Vector2()
    private var isBegan = false

    init {
        setAtlas(normalName)
    }

    val touchable = RectTouchable(Rect(Vector2(), size), camera) { _, type, check ->
        if (type.likeBegan() && check) {
            this.setAtlas(pressedName)
            isBegan = true
        }
        if (type.likeEnded()) {
            if (check && isBegan) {
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