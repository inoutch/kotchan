package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.shader.ShaderProgram
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkDescriptorSet
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkDescriptorSetLayout
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkPipeline
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkPipelineLayout

class GraphicsPipeline(
        val createInfo: CreateInfo,
        val vkBundle: VKBundle? = null) : Disposable {

    data class CreateInfo(val shaderProgram: ShaderProgram)

    data class VKBundle(
            val pipeline: VkPipeline,
            val descriptorSetLayout: VkDescriptorSetLayout,
            val descriptorSets: List<VkDescriptorSet>,
            val pipelineLayout: VkPipelineLayout)

    override fun dispose() {
        // only vulkan
        vkBundle?.descriptorSetLayout?.dispose()
        vkBundle?.pipeline?.dispose()
        vkBundle?.pipelineLayout?.dispose()
    }

    fun bind() {
        instance.graphicsApi.bindGraphicsPipeline(this)
    }
}
