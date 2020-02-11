package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram

class VKGraphicsPipeline(
        shaderProgram: ShaderProgram,
        config: GraphicsPipelineConfig,
        val pipeline: VKPipeline,
        val uniforms: List<VKUniform>,
        val uniformTextures: List<VKUniformTexture>,
        val descriptorSetLayout: VKDescriptorSetLayout,
        val descriptorPool: VKDescriptorPool,
        val descriptorSetUniformProviders: List<VKValuePerSwapchainImage<VKDescriptorSetUniformProvider>>,
        val descriptorSetTextureProviders: List<VKValuePerSwapchainImage<VKDescriptorSetTextureProvider>>
) : GraphicsPipeline(shaderProgram, config) {

    override fun bind() {
        // Provide descriptorSets
        var i = 0
        while (i < uniforms.size) {
            uniforms[i].bind(descriptorSetUniformProviders[i].value)
            i++
        }
        i = 0
        while (i < uniformTextures.size) {
            uniformTextures[i].bind(descriptorSetTextureProviders[i].value)
            i++
        }
    }
}
