package io.github.inoutch.kotchan.core.controller.touch.listener

import io.github.inoutch.kotchan.core.controller.touch.TouchType
import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.utility.type.Point

class ArgTouchListener(camera: Camera, private val argCallback: (index: Int, point: Point, type: TouchType, check: Boolean, chain: Boolean) -> Boolean) : TouchListener(camera) {
    override fun callback(index: Int, point: Point, type: TouchType, check: Boolean, chain: Boolean): Boolean {
        return argCallback(index, point, type, check, chain)
    }
}