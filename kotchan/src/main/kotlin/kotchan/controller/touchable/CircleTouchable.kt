package kotchan.controller.touchable

import interop.graphic.GLCamera
import kotchan.controller.TouchType
import utility.type.Circle
import utility.type.Vector2

open class CircleTouchable(
        circle: () -> Circle,
        camera: GLCamera,
        callback: (index: Int, point: Vector2, type: TouchType, check: Boolean) -> Unit) : Touchable(camera, callback) {
    override fun check(point: Vector2, camera: GLCamera): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}