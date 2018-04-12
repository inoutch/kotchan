package kotchan.texture

import interop.graphic.GLTexture
import kotchan.tool.TextureFrame
import utility.type.Vector2
import utility.type.Vector3

data class TextureAtlas(private val frames: List<TextureFrame>, val texture: GLTexture) {
    private val frameMeshes: List<Pair<List<Vector3>, List<Vector2>>>
    private val frameMap: Map<String, Int>

    init {
        frameMap = frames.mapIndexed { index, textureFrame -> Pair(textureFrame.filename, index) }.toMap()
        frameMeshes = frames.map {
            val sx = it.spriteSourceSize.origin.x
            val sy = it.spriteSourceSize.origin.y
            val sw = it.spriteSourceSize.size.x
            val sh = it.spriteSourceSize.size.y

            val w = texture.width.toFloat()
            val h = texture.height.toFloat()
            val fx = it.frame.origin.x / w
            val fy = it.frame.origin.y / h
            val fw = it.frame.size.x / w
            val fh = it.frame.size.y / h

            val positions = listOf(
                    Vector3(sx, sy, 0.0f),
                    Vector3(sx + sw, sy + sh, 0.0f),
                    Vector3(sx, sy + sh, 0.0f),
                    Vector3(sx, sy, 0.0f),
                    Vector3(sx + sw, sy, 0.0f),
                    Vector3(sx + sw, sy + sh, 0.0f))
            val texcoords = listOf(
                    Vector2(fx, fy + fh),
                    Vector2(fx + fw, fy),
                    Vector2(fx, fy),
                    Vector2(fx, fy + fh),
                    Vector2(fx + fw, fy + fh),
                    Vector2(fx + fw, fy))
            Pair(positions, texcoords)
        }
    }

    fun frameMesh(name: String): Pair<List<Vector3>, List<Vector2>>? {
        return frameMeshes[frameMap[name] ?: return null]
    }

    fun frame(name: String): TextureFrame? {
        return frames[frameMap[name] ?: return null]
    }
}