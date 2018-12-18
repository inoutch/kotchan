package io.github.inoutch.kotchan.core.view.ui

import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector4
import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.view.drawable.Square

class MockButton(camera: Camera, size: Vector2, onClick: () -> Unit) : Square(size) {

    var normalColor = Vector4(0.5f, 0.5f, 1.0f, 1.0f)
        set(value) {
            field = value
            color = value
        }

    var pressedColor = Vector4(0.2f, 0.2f, 1.0f, 1.0f)

    val touchListener = ButtonTouchListener(
            { color = normalColor },
            { color = pressedColor },
            camera) { onClick() }
            .apply { decision = RectTouchDecision { rect() } }

    init {
        color = normalColor
    }
}