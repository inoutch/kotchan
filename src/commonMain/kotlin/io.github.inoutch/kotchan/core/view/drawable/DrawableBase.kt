package io.github.inoutch.kotchan.core.view.drawable

import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3

interface DrawableBase {

    val size: Vector2

    var position: Vector3

    var anchorPoint: Vector2
}