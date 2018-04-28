package kotchan.ui

import kotchan.camera.Camera
import kotchan.controller.touchable.ButtonTouchable
import kotchan.scene.drawable.Sprite
import kotchan.texture.TextureAtlas

class Button(textureAtlas: TextureAtlas,
             private val normalName: String,
             private val pressedName: String,
             camera: Camera,
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