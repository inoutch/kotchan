package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram
import io.github.inoutch.kotlin.gl.api.GL_BLEND
import io.github.inoutch.kotlin.gl.api.GL_CULL_FACE
import io.github.inoutch.kotlin.gl.api.GL_DEPTH_TEST
import io.github.inoutch.kotlin.gl.api.GL_LESS
import io.github.inoutch.kotlin.gl.api.gl

class GLGraphicsPipeline(
    shaderProgram: ShaderProgram,
    config: GraphicsPipelineConfig,
    private val shader: GLShader,
    private val uniforms: List<GLUniform>,
    private val uniformTextures: List<GLUniformTexture>,
    private val uniformTextureArrays: List<GLUniformTextureArray>
) : GraphicsPipeline(shaderProgram, config) {
    private val uniformProviders = uniforms.map {
        GLUniformLocationProvider(shader, it.descriptorName)
    }

    private val uniformTextureProviders = uniformTextures.map {
        GLUniformLocationProvider(shader, it.descriptorName)
    }

    private val uniformTextureArrayProviders = uniformTextureArrays.map {
        GLUniformArrayLocationProvider(shader, it.descriptorName, it.size)
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
        i = 0
        while (i < uniformTextureArrays.size) {
            uniformTextureArrays[i].bind(uniformTextureArrayProviders[i])
            i++
        }

        if (config.depthTest) {
            gl.enable(GL_DEPTH_TEST)
            gl.depthFunc(GL_LESS)
        } else {
            gl.disable(GL_DEPTH_TEST)
        }

        gl.enable(GL_CULL_FACE)
        gl.cullFace(config.cullMode.toGLCullMode())

        gl.enable(GL_BLEND)
        gl.blendFunc(
                config.srcBlendFactor.toGLBlendFactor(),
                config.dstBlendFactor.toGLBlendFactor()
        )

        graphic.bindGraphicsPipeline(this)

        val shader = shaderProgram.shader
        check(shader is GLShader)
        shader.use()
    }
}
