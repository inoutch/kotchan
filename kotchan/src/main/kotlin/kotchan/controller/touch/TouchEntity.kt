package kotchan.controller.touch

import utility.type.Vector2

class TouchEntity(var index: Int, var point: Vector2, var type: TouchType) : Touch {
    override fun index() = index
    override fun point() = point
    override fun type() = type
}