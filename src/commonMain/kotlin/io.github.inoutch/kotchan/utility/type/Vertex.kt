package io.github.inoutch.kotchan.utility.type

import kotlinx.serialization.Serializable

@Serializable
data class Vertex(val position: Vector3, val color: Vector4, val texcoord: Vector2, val normal: Vector3? = null)
