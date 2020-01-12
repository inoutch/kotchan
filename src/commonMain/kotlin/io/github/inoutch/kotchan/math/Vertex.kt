package io.github.inoutch.kotchan.math

import kotlinx.serialization.Serializable

@Serializable
data class Vertex(
    val position: Vector3F,
    val color: Vector4F,
    val texcoord: Vector2F,
    val normal: Vector3F? = null
)
