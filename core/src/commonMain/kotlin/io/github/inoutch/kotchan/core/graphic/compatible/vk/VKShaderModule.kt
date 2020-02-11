package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkPipelineShaderStageCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkShaderModule
import io.github.inoutch.kotlin.vulkan.api.VkShaderStageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.vk

class VKShaderModule(
    val device: VKLogicalDevice,
    val shaderModule: VkShaderModule
) : Disposer() {
    init {
        add { vk.destroyShaderModule(device.device, shaderModule) }
    }

    fun createShaderStage(stage: VkShaderStageFlagBits): VkPipelineShaderStageCreateInfo {
        return VkPipelineShaderStageCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO,
                emptyList(),
                listOf(stage),
                shaderModule,
                "main",
                null
        )
    }
}
