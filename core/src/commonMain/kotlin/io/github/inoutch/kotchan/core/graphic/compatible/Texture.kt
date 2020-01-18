package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.math.Vector2I

abstract class Texture: Disposer() {
    companion object {
        fun loadFromImage(image: Image): Texture {
            return graphic.loadTexture(image)
        }
    }

    abstract val size: Vector2I
    open var addressModeU: TextureAddressMode = TextureAddressMode.REPEAT
    open var addressModeV: TextureAddressMode = TextureAddressMode.REPEAT
    open var magFilter: TextureFilter = TextureFilter.LINEAR
    open var minFilter: TextureFilter = TextureFilter.LINEAR
    open var mipmapMode: TextureMipmapMode = TextureMipmapMode.LINER
}
