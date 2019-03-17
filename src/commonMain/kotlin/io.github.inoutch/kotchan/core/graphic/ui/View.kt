package io.github.inoutch.kotchan.core.graphic.ui

import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3

interface View {
    val size: Vector2

    val position: Vector3

    val anchorPoint: Vector2
}
