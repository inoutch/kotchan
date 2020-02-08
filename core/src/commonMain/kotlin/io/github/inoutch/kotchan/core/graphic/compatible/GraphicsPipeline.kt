package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram

abstract class GraphicsPipeline(
        val shaderProgram: ShaderProgram,
        val config: GraphicsPipelineConfig = GraphicsPipelineConfig()
): Disposer() {
    abstract fun bind()
}
