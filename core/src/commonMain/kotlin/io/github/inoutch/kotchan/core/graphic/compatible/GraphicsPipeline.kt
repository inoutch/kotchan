package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram

abstract class GraphicsPipeline(
        val shaderProgram: ShaderProgram,
        val config: GraphicsPipelineConfig = GraphicsPipelineConfig()
): Disposer() {
    companion object {
        fun create(
                shaderProgram: ShaderProgram,
                config: GraphicsPipelineConfig = GraphicsPipelineConfig()
        ): GraphicsPipeline {
            return graphic.createGraphicsPipeline(shaderProgram, config)
        }
    }

    abstract fun bind()
}
