package kotchan.ui

import interop.graphic.GLCamera
import kotchan.controller.TouchType
import kotchan.controller.touchable.ButtonTouchable
import kotchan.controller.touchable.RectTouchable
import kotchan.logger.logger
import kotchan.scene.drawable.Sprite
import kotchan.texture.TextureAtlas
import utility.type.Rect
import utility.type.Vector2

class Button(textureAtlas: TextureAtlas,
                      private val normalName: String,
                      private val pressedName: String,
                      camera: GLCamera,
                      onClick: () -> Unit) : Sprite(textureAtlas) {
    init {
        setAtlas(normalName)
    }

    val touchable = ButtonTouchable(
            { rect() },
            { setAtlas(normalName) },
            { setAtlas(pressedName) },
            camera) { onClick() }
}