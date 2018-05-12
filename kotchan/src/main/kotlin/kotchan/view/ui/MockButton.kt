package kotchan.view.ui

import kotchan.view.camera.Camera
import kotchan.controller.touch.listener.ButtonTouchListener
import kotchan.controller.touch.listener.decision.RectTouchDecision
import kotchan.view.drawable.Square
import utility.type.Vector2
import utility.type.Vector4

class MockButton(camera: Camera, size: Vector2, onClick: () -> Unit) : Square(size) {
    var normalColor = Vector4(0.5f, 0.5f, 1.0f, 1.0f)
        set(value) {
            field = value
            color = value
        }
    var pressedColor = Vector4(0.2f, 0.2f, 1.0f, 1.0f)

    init {
        color = normalColor
    }

    val touchListener = ButtonTouchListener(
            { color = normalColor },
            { color = pressedColor },
            camera) { onClick() }
            .apply { decision = RectTouchDecision({ rect() }) }
}