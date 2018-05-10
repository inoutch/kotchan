package kotchan.controller.touch.listener

import kotchan.controller.touch.TouchType
import kotchan.view.camera.Camera
import utility.type.Vector2

class ArgTouchListener(camera: Camera, private val argCallback: (index: Int, point: Vector2, type: TouchType, check: Boolean, chain: Boolean) -> Boolean) : TouchListener(camera) {
    override fun callback(index: Int, point: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean {
        return argCallback(index, point, type, check, chain)
    }
}