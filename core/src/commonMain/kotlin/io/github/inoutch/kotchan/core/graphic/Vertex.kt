package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

data class Vertex(
        val position: Vector3F,
        val color: Vector4F,
        val texcoord: Vector2F,
        val normal: Vector3F? = null
)
