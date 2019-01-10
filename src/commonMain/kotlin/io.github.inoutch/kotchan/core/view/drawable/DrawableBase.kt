package io.github.inoutch.kotchan.core.view.drawable

import io.github.inoutch.kotchan.core.model.Model
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3

interface DrawableBase : Model {

    val size: Vector2

    var position: Vector3

    var anchorPoint: Vector2

    var visible: Boolean
}