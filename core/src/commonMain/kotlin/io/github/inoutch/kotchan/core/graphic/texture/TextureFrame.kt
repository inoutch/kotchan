package io.github.inoutch.kotchan.core.graphic.texture

import io.github.inoutch.kotchan.math.RectF
import io.github.inoutch.kotchan.math.Vector2F

data class TextureFrame(
    val filename: String,
    val frame: RectF,
    val rotated: Boolean,
    val trimmed: Boolean,
    val spriteSourceSize: RectF,
    val sourceSize: Vector2F
)
