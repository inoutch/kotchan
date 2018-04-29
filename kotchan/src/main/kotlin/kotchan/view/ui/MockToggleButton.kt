package kotchan.view.ui

import kotchan.view.camera.Camera
import kotchan.controller.touch.listener.ButtonTouchListener
import kotchan.view.drawable.Square
import utility.type.Vector2
import utility.type.Vector4

class MockToggleButton(
        camera: Camera,
        size: Vector2,
        onToggleOn: () -> Unit,
        onToggleOff: () -> Unit) : Square(size) {
    var onColor = Vector4(0.5f, 0.5f, 1.0f, 1.0f)
    var offColor = Vector4(0.2f, 0.2f, 1.0f, 1.0f)
    var state = true

    init {
        setColor()
    }

    val touchListener = ButtonTouchListener(
            { rect() }, { }, { },
            camera) {
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