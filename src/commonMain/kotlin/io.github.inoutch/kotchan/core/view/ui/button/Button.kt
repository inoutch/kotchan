package io.github.inoutch.kotchan.core.view.ui

import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.view.drawable.Sprite
import io.github.inoutch.kotchan.core.view.texture.TextureAtlas

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