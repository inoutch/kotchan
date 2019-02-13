package io.github.inoutch.kotchan.core.graphic.ui.button

import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.graphic.view.Sprite
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas

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
            .apply { decision = RectTouchDecision { rect() } }
}
