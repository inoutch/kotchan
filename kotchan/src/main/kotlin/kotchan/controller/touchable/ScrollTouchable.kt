package kotchan.controller.touchable

import interop.graphic.GLCamera
import kotchan.Engine
import kotchan.controller.TouchType
import utility.type.Vector2

class ScrollTouchable(camera: GLCamera, private var position: Vector2, private val setPosition: (position: Vector2) -> Unit) : Touchable(camera) {
    private var before: Vector2? = null
    override fun check(point: Vector2, camera: GLCamera): Boolean = true
    override fun callback(index: Int, point: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean {
        if (index != 0) {
            // only single touch
            return true
        }
        // point is -1.0 ~ 1.0
        val after = point * Engine.getInstance().screenSize / 2.0f
        if (type == TouchType.Began && chain) {
            before = after
        }
        before?.let {
            when (type) {
                TouchType.Moved -> {
                    position += after - it
                    before = after
                }
                TouchType.Ended -> {
                    position += after - it
                    before = null
                }
                TouchType.Cancelled -> {
                    before = null
                }
                else -> {
                }
            }
            setPosition(position)
        }
        return true
    }
}