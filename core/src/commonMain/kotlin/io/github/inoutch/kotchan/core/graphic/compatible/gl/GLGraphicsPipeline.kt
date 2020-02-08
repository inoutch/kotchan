package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram
import io.github.inoutch.kotlin.gl.api.GL_DEPTH_TEST
import io.github.inoutch.kotlin.gl.api.gl

class GLGraphicsPipeline(
        shaderProgram: ShaderProgram,
        config: GraphicsPipelineConfig
) : GraphicsPipeline(shaderProgram, config) {
    override fun bind() {
        if (config.depthTest) {
            gl.enable(GL_DEPTH_TEST)
        } else {
            gl.enable(GL_DEPTH_TEST)
        }

        val shader = shaderProgram.shader
        check(shader is GLShader)
        shader.use()
    }
}