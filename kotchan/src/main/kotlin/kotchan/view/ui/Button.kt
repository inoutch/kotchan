package kotchan.view.ui

import kotchan.view.camera.Camera
import kotchan.controller.touch.listener.ButtonTouchListener
import kotchan.controller.touch.listener.decision.RectTouchDecision
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
            { setAtlas(normalName) },
            { setAtlas(pressedName) },
            camera) { onClick() }
            .apply { decision = RectTouchDecision({ rect() }) }
}