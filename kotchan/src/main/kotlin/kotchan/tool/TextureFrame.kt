package kotchan.tool

import utility.type.Rect
import utility.type.Vector2

data class TextureFrame(
        val filename: String,
        val frame: Rect,
        val rotated: Boolean,
        val trimmed: Boolean,
        val spriteSourceSize: Rect,
        val sourceSize: Vector2)