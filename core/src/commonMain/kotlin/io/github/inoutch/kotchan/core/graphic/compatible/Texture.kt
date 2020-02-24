package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.math.Vector2I

abstract class Texture(val config: Config) : Disposer() {
    companion object {
        fun load(image: Image, config: Config = Config()): Texture {
            return graphic.loadTexture(image, config)
        }

        @ExperimentalStdlibApi
        suspend fun loadFromResourceWithError(filepath: String, config: Config = Config()): Texture {
            val image = Image.loadFromResourceWithError(filepath)
            return load(image, config)
        }

        suspend fun load(filepath: String, config: Config = Config()): Texture? {
            val image = Image.load(filepath) ?: return null
            return load(image, config)
        }

        fun empty(): Texture {
            return graphic.emptyTexture()
        }
    }

    data class Config(
            val addressModeU: TextureAddressMode = TextureAddressMode.CLAMP_TO_EDGE,
            val addressModeV: TextureAddressMode = TextureAddressMode.CLAMP_TO_EDGE,
            val magFilter: TextureFilter = TextureFilter.LINEAR,
            val minFilter: TextureFilter = TextureFilter.LINEAR,
            val mipmapMode: TextureMipmapMode = TextureMipmapMode.LINER
    )

    abstract val size: Vector2I
}
