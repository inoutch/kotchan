package kotchan.ui

import interop.graphic.GLCamera
import kotchan.controller.touchable.ButtonTouchable
import kotchan.scene.drawable.Square
import utility.type.Vector2
import utility.type.Vector4

class MockButton(camera: GLCamera, size: Vector2, onClick: () -> Unit) : Square(size) {
    var normalColor = Vector4(0.5f, 0.5f, 1.0f, 1.0f)
    var pressedColor = Vector4(0.2f, 0.2f, 1.0f, 1.0f)

    init {
        color = normalColor
    }

    val touchable = ButtonTouchable(
            { rect() },
            { color = normalColor },
            { color = pressedColor },
            camera) {
        onClick()
    }
}