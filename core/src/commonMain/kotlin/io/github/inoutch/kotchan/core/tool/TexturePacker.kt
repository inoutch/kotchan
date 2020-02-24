package io.github.inoutch.kotchan.core.tool

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.file
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.core.graphic.texture.TextureFrame
import io.github.inoutch.kotchan.core.io.file.getResourcePathWithError
import io.github.inoutch.kotchan.core.io.file.readTextAsync
import io.github.inoutch.kotchan.math.RectF
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.utility.Path
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class TexturePacker private constructor() {
    companion object {
        @ExperimentalStdlibApi
        suspend fun loadWithError(textureDir: String, filepath: String): Bundle {
            val text = file.readTextAsync(filepath).await()
            checkNotNull(text) { "Failed to load $filepath" }

            val json = Json(JsonConfiguration.Stable)
            val root = json.parse(Root.serializer(), text)

            val imagePath = Path.resolve(textureDir, root.meta.image)
            val texture = Texture.loadFromFile(imagePath)
            checkNotNull(texture) { "Failed to load $imagePath" }

            val textureFrames = root.frames.map {
                TextureFrame(
                        it.key,
                        it.value.frame.toRectF(),
                        it.value.rotated,
                        it.value.trimmed,
                        it.value.spriteSourceSize.toRectF(),
                        it.value.sourceSize.toVector2F()
                )
            }
            return Bundle(TextureAtlas(textureFrames, texture.size), texture)
        }

        @ExperimentalStdlibApi
        suspend fun loadFromResourceWithError(textureDir: String, filepath: String) =
                loadWithError(file.getResourcePathWithError(textureDir), file.getResourcePathWithError(filepath))
    }

    data class Bundle(val textureAtlas: TextureAtlas, val texture: Texture)

    @Serializable
    data class FrameRect(
            val x: Float,
            val y: Float,
            val w: Float,
            val h: Float
    ) {
        fun toRectF(): RectF {
            return RectF(Vector2F(x, y), Vector2F(w, h))
        }
    }

    @Serializable
    data class Frame(
            val frame: FrameRect,
            val rotated: Boolean,
            val trimmed: Boolean,
            val spriteSourceSize: FrameRect,
            val sourceSize: Size
    )

    @Serializable
    data class Size(
            val w: Float,
            val h: Float
    ) {
        fun toVector2F(): Vector2F {
            return Vector2F(w, h)
        }
    }

    @Serializable
    data class Meta(
            val app: String,
            val version: Float,
            val image: String,
            val format: String,
            val size: Size,
            val scale: String
    )

    @Serializable
    data class Root(val frames: Map<String, Frame>, val meta: Meta)
}
