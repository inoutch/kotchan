package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram
import io.github.inoutch.kotlin.gl.api.GL_DEPTH_TEST
import io.github.inoutch.kotlin.gl.api.gl

class GLGraphicsPipeline(
        shaderProgram: ShaderProgram,
        config: GraphicsPipelineConfig,
        private val shader: GLShader,
        private val uniforms: List<GLUniform>,
        private val uniformTextures: List<GLUniformTexture>
) : GraphicsPipeline(shaderProgram, config) {
    private val uniformProviders = uniforms.map {
        GLUniformLocationProvider(shader, it.descriptorName)
    }

    private val uniformTextureProviders = uniformTextures.map {
        GLUniformLocationProvider(shader, it.descriptorName)
    }

    override fun bind() {
        // Provide locations
        var i = 0
        while (i < uniforms.size) {
            uniforms[i].bind(uniformProviders[i])
            i++
        }
        i = 0
        while (i < uniformTextures.size) {
            uniformTextures[i].bind(uniformTextureProviders[i])
            i++
        }

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