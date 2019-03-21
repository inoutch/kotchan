package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkPipelineVertexInputStateCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo,
        scope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .pVertexBindingDescriptions(vertexBindingDescriptions.toNative(scope))
            .pVertexAttributeDescriptions(vertexAttributeDescriptions.toNative(scope))
}

fun VkPipelineVertexInputStateCreateInfo.toNative(scope: MemScope)
        : org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo =
        scope.add(org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo.calloc()
                .also { copyToNative(it, scope) })
