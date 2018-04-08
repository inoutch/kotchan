package kotchan.controller

import utility.type.Vector2

class TouchEntity(var point: Vector2, var type: TouchType) : Touch {
    override fun type(): TouchType = type
}