package io.github.inoutch.kotchan.core.controller.touch.listener

import io.github.inoutch.kotchan.core.controller.touch.TouchType
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.utility.type.Vector2

open class ArgTouchListener(camera: Camera, private val argCallback: (index: Int, normalizedPoint: Vector2, type: TouchType, check: Boolean, chain: Boolean) -> Boolean) : TouchListener(camera) {
    override fun callback(index: Int, normalizedPoint: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean {
        return argCallback(index, normalizedPoint, type, check, chain)
    }
}
