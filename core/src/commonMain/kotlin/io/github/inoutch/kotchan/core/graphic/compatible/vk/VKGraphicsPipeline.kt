package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram

class VKGraphicsPipeline(
        shaderProgram: ShaderProgram,
        config: GraphicsPipelineConfig,
        val pipeline: VKPipeline,
        val uniforms: List<VKUniform>,
        val samplers: List<VKSampler>
) : GraphicsPipeline(shaderProgram, config) {
    init {
        add(pipeline)
    }

    override fun bind() {
    }
}