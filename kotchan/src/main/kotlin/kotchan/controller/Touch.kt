package kotchan.controller

import utility.type.Vector2

interface Touch {
    fun index(): Int
    fun point(): Vector2
    fun type(): TouchType
}