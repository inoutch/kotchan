package io.github.inoutch.kotchan.core.graphic.ui.button.toggle

import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector4
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.graphic.view.Square

class MockToggleButton(
        camera: Camera,
        size: Vector2,
        private val onToggleOn: () -> Unit,
        private val onToggleOff: () -> Unit) : Square(size) {

    var onColor = Vector4(0.5f, 0.5f, 1.0f, 1.0f)

    var offColor = Vector4(0.2f, 0.2f, 1.0f, 1.0f)

    var state = true
        set(value) {
            field = value
            setColor()
        }

    val touchListener = ButtonTouchListener({ }, { }, camera) { click() }
            .apply { decision = RectTouchDecision { rect() } }

    init {
        setColor()
    }

    private fun click() {
        state = !state
        if (state) {
            onToggleOn()
        } else {
            onToggleOff()
        }
        setColor()
    }

    private fun setColor() {
        color = if (state) onColor else offColor
    }
}
