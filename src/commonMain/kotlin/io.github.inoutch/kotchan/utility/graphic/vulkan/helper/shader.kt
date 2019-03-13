package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.graphic.vulkan.VkPipelineShaderStageCreateInfo
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkShaderModule
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkShaderStageFlagBits

fun createShaderStages(vertShaderModule: VkShaderModule, fragShaderModule: VkShaderModule):
        List<VkPipelineShaderStageCreateInfo> {
    val vertShaderState = VkPipelineShaderStageCreateInfo(
            0,
            listOf(VkShaderStageFlagBits.VK_SHADER_STAGE_VERTEX_BIT),
            vertShaderModule,
            "main",
            null)
    val fragShaderStage = VkPipelineShaderStageCreateInfo(
            0,
            listOf(VkShaderStageFlagBits.VK_SHADER_STAGE_FRAGMENT_BIT),
            fragShaderModule,
            "main",
            null)
    return listOf(vertShaderState, fragShaderStage)
}
