package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkPipelineVertexInputStateCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo,
        scope: MemScope) {
    native.flags(flags)
            .pVertexBindingDescriptions(vertexBindingDescriptions.toNative(scope))
            .pVertexAttributeDescriptions(vertexAttributeDescriptions.toNative(scope))
}

fun VkPipelineVertexInputStateCreateInfo.toNative(scope: MemScope)
        : org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo =
        scope.add(org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo.calloc()
                .also { copyToNative(it, scope) })
