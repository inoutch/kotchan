package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineVertexInputStateCreateInfo.copyToNative(
        native: vulkan.VkPipelineVertexInputStateCreateInfo,
        scope: MemScope) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO
    native.flags = flags.toUInt()
    native.vertexBindingDescriptionCount = vertexBindingDescriptions.size.toUInt()
    native.pVertexBindingDescriptions = vertexBindingDescriptions.toNative(scope)
    native.vertexAttributeDescriptionCount = vertexAttributeDescriptions.size.toUInt()
    native.pVertexAttributeDescriptions = vertexAttributeDescriptions.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkPipelineVertexInputStateCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineVertexInputStateCreateInfo>().also { copyToNative(it, scope) }.ptr
