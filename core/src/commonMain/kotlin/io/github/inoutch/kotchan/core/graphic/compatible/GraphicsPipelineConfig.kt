package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.graphic.BlendFactor
import io.github.inoutch.kotchan.core.graphic.CullMode
import io.github.inoutch.kotchan.core.graphic.PolygonMode

data class GraphicsPipelineConfig(
        val depthTest: Boolean = true,
        val cullMode: CullMode = CullMode.Front,
        val polygonMode: PolygonMode = PolygonMode.Fill,
        val srcBlendFactor: BlendFactor = BlendFactor.SrcAlpha,
        val dstBlendFactor: BlendFactor = BlendFactor.OneMinusSrcAlpha
)