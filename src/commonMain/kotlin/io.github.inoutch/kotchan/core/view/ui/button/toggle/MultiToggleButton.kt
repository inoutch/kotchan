package io.github.inoutch.kotchan.core.view.ui.button.toggle

import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.core.view.drawable.Sprite
import io.github.inoutch.kotchan.core.view.texture.TextureAtlas

class MultiToggleButton(
        textureAtlas: TextureAtlas,
        uiCamera: Camera,
        private val names: List<String>,
        private val onToggle: (state: Int) -> Unit) : Sprite(textureAtlas) {
    var state = 0
        private set

    val touchListener = ButtonTouchListener({ }, { }, uiCamera) { click() }
            .apply { decision = RectTouchDecision { rect() } }

    init {
        setAtlas(names.first())
    }

    private fun click() {
        state = (state + 1) % names.size
        setAtlas(names[state])
        onToggle(state)
    }
}