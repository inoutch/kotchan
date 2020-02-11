package io.github.inoutch.kotchan.core.graphic.material

import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram

abstract class MaterialBase<T : ShaderProgram>(
    protected val shaderProgram: T,
    protected val graphicsPipelineConfig: GraphicsPipelineConfig
) : Material {
    private val graphicsPipeline = GraphicsPipeline.create(
            shaderProgram,
            graphicsPipelineConfig
    )

    override fun bind() {
        graphicsPipeline.bind()
    }
}
