package kotchan.view.ui

import kotchan.view.camera.Camera
import kotchan.controller.touch.listener.ButtonTouchListener
import kotchan.view.drawable.Sprite
import kotchan.view.texture.TextureAtlas

class Button(textureAtlas: TextureAtlas,
             private val normalName: String,
             private val pressedName: String,
             camera: Camera,
             onClick: () -> Unit) : Sprite(textureAtlas) {
    init {
        setAtlas(normalName)
    }

    val touchListener = ButtonTouchListener(
            { rect() },
            { setAtlas(normalName) },
            { setAtlas(pressedName) },
            camera) { onClick() }
}