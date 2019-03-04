package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkPipelineLayoutCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkPipelineLayoutCreateInfo,
        scope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .pSetLayouts(setLayouts.map { it.native }.toLongArray().toNative(scope))
            .pPushConstantRanges(pushConstantRanges.toNative(scope))
}

fun VkPipelineLayoutCreateInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkPipelineLayoutCreateInfo =
        memScope.add(org.lwjgl.vulkan.VkPipelineLayoutCreateInfo.calloc()
                .also { copyToNative(it, memScope) })
