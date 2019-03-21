package io.github.inoutch.kotchan.core.tool

import io.github.inoutch.kotchan.utility.data.json.Json
import interop.data.json.JsonType
import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.KotchanCore.Companion.logger
import io.github.inoutch.kotchan.core.error.NoSuchFileError
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.utility.path.Path
import io.github.inoutch.kotchan.utility.type.*

class TexturePacker {
    data class Bundle(val textureAtlas: TextureAtlas, val texture: Texture)

    companion object {
        fun loadFileFromResource(textureDir: String, filepath: String, enableCache: Boolean = true) =
                loadFile(instance.file.getResourcePath(textureDir) ?: throw NoSuchFileError(textureDir),
                        instance.file.getResourcePath(filepath) ?: throw NoSuchFileError(filepath), enableCache)
                        ?: throw NoSuchFileError(Path.resolve(textureDir, filepath))

        fun loadFile(textureDir: String, filepath: String, enableCache: Boolean = true): Bundle? {
            val file = KotchanCore.instance.file
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
            val texture = KotchanCore.instance.let {
                if (enableCache) {
                    it.textureCacheManager.load(imagePath)
                } else {
                    Texture.load(imagePath)
                }
            }
            if (texture == null) {
                logger.warn("texture is not found [$imagePath]")
                return null
            }
            return Bundle(TextureAtlas(frames, texture.size.toVector2()), texture)
        }
    }
}
