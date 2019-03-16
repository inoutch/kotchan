package io.github.inoutch.kotchan.core.graphic.texture

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.gl.GLTexture
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKTexture
import io.github.inoutch.kotchan.utility.type.Point

class Texture(val vkTexture: VKTexture? = null,
              val glTexture: GLTexture? = null) : Disposable {
    val size: Point
        get() = vkTexture?.size ?: glTexture?.let { Point(it.width, it.height) } ?: Point.ZERO

    override fun dispose() {
        vkTexture?.dispose()
        glTexture?.dispose()
    }
}
