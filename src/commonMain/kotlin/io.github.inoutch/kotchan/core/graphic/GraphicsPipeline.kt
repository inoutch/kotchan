package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.shader.ShaderProgram
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkDescriptorSetLayout
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkPipeline
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkPipelineLayout
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKDescriptorSetStack

class GraphicsPipeline(
        val createInfo: CreateInfo,
        val vkBundle: VKBundle? = null) : Disposable {

    data class CreateInfo(
            val shaderProgram: ShaderProgram,
            val depthTest: Boolean = true,
            val cullMode: CullMode = CullMode.Back,
            val polygonMode: PolygonMode = PolygonMode.Fill)

    data class VKBundle(
            val pipeline: VkPipeline,
            val descriptorSetLayout: VkDescriptorSetLayout,
            val descriptorSetStacks: List<VKDescriptorSetStack>,
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
