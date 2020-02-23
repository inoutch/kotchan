package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.math.Vector2I
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

abstract class Texture(val config: Config) : Disposer() {
    companion object {
        fun loadFromImage(image: Image, config: Config = Config()): Texture {
            return graphic.loadTexture(image, config)
        }

        fun loadFromFileAsync(filepath: String, config: Config = Config()) = GlobalScope.async {
            val image = Image.loadFromFileAsync(filepath).await() ?: return@async null
            loadFromImage(image, config)
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
