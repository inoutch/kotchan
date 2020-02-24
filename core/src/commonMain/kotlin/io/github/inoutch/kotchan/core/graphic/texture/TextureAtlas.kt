package io.github.inoutch.kotchan.core.graphic.texture

import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector3F

data class TextureAtlas(
    private val frames: List<TextureFrame>,
    val textureSize: Vector2I
) {

    private val frameMeshes: List<Pair<List<Vector3F>, List<Vector2F>>>

    private val frameMap: Map<String, Int>

    init {
        frameMap = frames.mapIndexed { index, textureFrame ->
            Pair(textureFrame.filename, index)
        }.toMap()
        frameMeshes = frames.map {
            val sx = it.spriteSourceSize.origin.x
            val sy = it.spriteSourceSize.origin.y
            val sw = it.spriteSourceSize.size.x
            val sh = it.spriteSourceSize.size.y

            val w = textureSize.x
            val h = textureSize.y
            val fx = it.frame.origin.x / w
            val fy = it.frame.origin.y / h
            val fw = it.frame.size.x / w
            val fh = it.frame.size.y / h

            val positions = listOf(
                    Vector3F(sx, sy, 0.0f),
                    Vector3F(sx + sw, sy + sh, 0.0f),
                    Vector3F(sx, sy + sh, 0.0f),
                    Vector3F(sx, sy, 0.0f),
                    Vector3F(sx + sw, sy, 0.0f),
                    Vector3F(sx + sw, sy + sh, 0.0f)
            )
            val texcoords = listOf(
                    Vector2F(fx, fy + fh),
                    Vector2F(fx + fw, fy),
                    Vector2F(fx, fy),
                    Vector2F(fx, fy + fh),
                    Vector2F(fx + fw, fy + fh),
                    Vector2F(fx + fw, fy)
            )
            Pair(positions, texcoords)
        }
    }

    fun frameMesh(name: String): Pair<List<Vector3F>, List<Vector2F>>? {
        return frameMeshes[frameMap[name] ?: return null]
    }

    fun frameMesh(index: Int): Pair<List<Vector3F>, List<Vector2F>>? {
        return frameMeshes.getOrNull(index)
    }

    fun frame(name: String): TextureFrame? {
        return frames[frameMap[name] ?: return null]
    }

    fun frame(index: Int): TextureFrame? {
        return frames.getOrNull(index)
    }

    fun indexOf(name: String): Int {
        val frame = frame(name) ?: return -1
        return indexOf(frame)
    }

    fun indexOf(frame: TextureFrame): Int {
        return frames.indexOf(frame)
    }
}
