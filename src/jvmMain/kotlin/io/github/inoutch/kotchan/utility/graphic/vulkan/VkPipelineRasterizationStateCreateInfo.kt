package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkPipelineRasterizationStateCreateInfo.copyToNative(native: org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo) {
    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .polygonMode(polygonMode.value)
            .cullMode(cullMode.value)
            .frontFace(frontFace.value)
            .depthClampEnable(depthClampEnable)
            .rasterizerDiscardEnable(rasterizerDiscardEnable)
            .depthBiasEnable(depthBiasEnable)
            .depthBiasConstantFactor(depthBiasConstantFactor)
            .depthBiasClamp(depthBiasClamp)
            .depthBiasSlopeFactor(depthBiasSlopeFactor)
            .lineWidth(lineWidth)
}

fun VkPipelineRasterizationStateCreateInfo.toNative(memScope: MemScope):
        org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo =
        memScope.add(org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo.calloc()
                .also { copyToNative(it) })
