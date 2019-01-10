package io.github.inoutch.kotchan.core.view.ui.button.toggle

import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.core.view.drawable.Sprite
import io.github.inoutch.kotchan.core.view.texture.TextureAtlas

class AnimatedToggleButton(
        textureAtlas: TextureAtlas,
        uiCamera: Camera,
        private val interval: Float,
        private val onAtlasNames: List<String>,
        private val offAtlasNames: List<String>,
        override val onToggleOn: () -> Unit,
        override val onToggleOff: () -> Unit) : Sprite(textureAtlas), ToggleButton {

    override var state: Boolean = true
        set(value) {
            field = value
            setAtlas(currentList().first())
            time = 0.0f
        }

    val touchListener = ButtonTouchListener({ }, { }, uiCamera) { click() }
            .apply { decision = RectTouchDecision { rect() } }

    private var time = 0.0f

    private var prevAnimationIndex = -1

    init {
        setAtlas(onAtlasNames.first())
    }

    override fun update(delta: Float) {
        time += delta
        val animationIndex = (time / interval).toInt() % currentList().size
        if (animationIndex != prevAnimationIndex) {
            setAtlas(currentList()[animationIndex])
            prevAnimationIndex = animationIndex
        }
    }

    private fun currentList() = if (state) onAtlasNames else offAtlasNames
}