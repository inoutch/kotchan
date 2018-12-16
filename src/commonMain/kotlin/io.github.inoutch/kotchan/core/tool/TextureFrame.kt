package io.github.inoutch.kotchan.core.tool

import io.github.inoutch.kotchan.utility.type.Rect
import io.github.inoutch.kotchan.utility.type.Vector2

data class TextureFrame(
        val filename: String,
        val frame: Rect,
        val rotated: Boolean,
        val trimmed: Boolean,
        val spriteSourceSize: Rect,
        val sourceSize: Vector2)