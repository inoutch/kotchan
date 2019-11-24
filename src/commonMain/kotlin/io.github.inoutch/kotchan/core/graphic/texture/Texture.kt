package io.github.inoutch.kotchan.core.graphic.texture

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.core.error.NoSuchFileError
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.gl.GLTexture
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKTexture
import io.github.inoutch.kotchan.utility.io.getResourcePathWithError
import io.github.inoutch.kotchan.utility.type.Point

class Texture private constructor(
    val vkTexture: VKTexture? = null,
    val glTexture: GLTexture? = null
) : Disposable {
    companion object {
        fun load(filepath: String) = core.graphicsApi.loadTexture(filepath)
                ?.let { Texture(it.vkTexture, it.glTexture) }

        fun loadFromResource(filepath: String) =
                load(core.file.getResourcePathWithError(filepath)) ?: throw NoSuchFileError(filepath)

        fun emptyTexture() = core.graphicsApi.emptyTexture { Texture(it.vkTexture, it.glTexture) }
    }

    val size: Point
        get() = vkTexture?.size ?: glTexture?.let { Point(it.width, it.height) } ?: Point.ZERO

    override fun dispose() {
        vkTexture?.dispose()
        glTexture?.dispose()
    }
}
