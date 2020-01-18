package io.github.inoutch.kotchan.core.graphic.compatible.renderer

import io.github.inoutch.kotchan.core.graphic.compatible.Texture

interface Renderer {
    fun begin()
    fun end()
    fun createTexture(): Texture
}
