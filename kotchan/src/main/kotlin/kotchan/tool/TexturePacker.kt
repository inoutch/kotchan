package kotchan.tool

import interop.data.json.Json
import interop.data.json.JsonType
import interop.graphic.GLTexture
import kotchan.Engine
import kotchan.logger.logger
import kotchan.view.texture.TextureAtlas
import utility.path.Path
import utility.type.Rect
import utility.type.*

class TexturePacker {
    companion object {
        fun loadFile(textureDir: String, filepath: String, enableCache: Boolean = true): TextureAtlas? {
            val file = Engine.getInstance().file
            val json = file.readText(filepath)
            if (json == null) {
                logger.error("this file is not json [$filepath]")
                return null
            }
            val root = Json.parse(json) ?: return null // parse error
            if (!root.isMap() && !root.isList()) {
                logger.error("incorrect json format [$root]")
                return null // not map or list
            }

            val rootScheme = listOf(
                    "frames" to JsonType.MapType,
                    "meta" to JsonType.MapType)
            val frameScheme = listOf(
                    "frame" to JsonType.MapType,
                    "rotated" to JsonType.FloatType,
                    "trimmed" to JsonType.FloatType,
                    "spriteSourceSize" to JsonType.MapType,
                    "sourceSize" to JsonType.MapType)
            val rectScheme = listOf(
                    "x" to JsonType.FloatType,
                    "y" to JsonType.FloatType,
                    "w" to JsonType.FloatType,
                    "h" to JsonType.FloatType)
            val sizeScheme = listOf(
                    "w" to JsonType.FloatType,
                    "h" to JsonType.FloatType)
            val metaScheme = listOf("image" to JsonType.TextType)

            val rootMap = root.byScheme(rootScheme)
            if (rootMap == null) {
                logger.error("incorrect json format [$root, $rootScheme]")
                return null
            }
            val frames = rootMap[0].toMap()
                    .filter { it.value.hasKeys(frameScheme) }
                    .mapNotNull {
                        val parent = it.value.toMap()
                        val children = frameScheme.map { parent[it.first] }
                        val frame = children[0]?.byScheme(rectScheme)?.mapNotNull { it.toFloat() }
                        val rotated = children[1]?.toBoolean()
                        val trimmed = children[2]?.toBoolean()
                        val spriteSourceSize = children[3]?.byScheme(rectScheme)?.mapNotNull { it.toFloat() }
                        val sourceSize = children[4]?.byScheme(sizeScheme)?.mapNotNull { it.toFloat() }

                        if (frame == null || rotated == null || trimmed == null ||
                                spriteSourceSize == null || sourceSize == null) {
                            return@mapNotNull null
                        }
                        TextureFrame(it.key,
                                Rect(frame.toVector2(), frame.toVector2(2)),
                                rotated, trimmed,
                                Rect(spriteSourceSize.toVector2(), spriteSourceSize.toVector2(2)),
                                sourceSize.toVector2())
                    }
            val meta = rootMap[1].byScheme(metaScheme) ?: return null
            val image = meta[0].toText() ?: return null
            val imagePath = Path.resolve(textureDir, image)
            val texture = if (enableCache) {
                Engine.getInstance().textureManager.get(imagePath)
            } else {
                Engine.getInstance().gl.loadTexture(imagePath)
            }
            if (texture == null) {
                logger.warn("texture is not found [$imagePath]")
            }
            return TextureAtlas(frames, texture ?: GLTexture.empty)
        }
    }
}