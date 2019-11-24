package io.github.inoutch.kotchan.core.controller.touch

import io.github.inoutch.kotchan.utility.type.Point

class TouchEntity(var index: Int, var point: Point, var type: TouchType) : Touch {

    override fun index() = index

    override fun point() = point

    override fun type() = type
}
