package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.core.graphic.texture.Texture

data class Material(val graphicsPipeline: GraphicsPipeline, val texture: Texture? = null)
