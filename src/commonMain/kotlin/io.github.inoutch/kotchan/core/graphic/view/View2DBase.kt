package io.github.inoutch.kotchan.core.graphic.view

import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3

interface View2DBase {

    var position: Vector3

    val size: Vector2

    var anchorPoint: Vector2

    var visible: Boolean
}
