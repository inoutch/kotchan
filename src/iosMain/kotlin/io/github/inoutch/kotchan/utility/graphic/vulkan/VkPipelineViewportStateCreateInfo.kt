package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineViewportStateCreateInfo.copyToNative(
        native: vulkan.VkPipelineViewportStateCreateInfo,
        scope: MemScope) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.viewportCount = viewportCount.toUInt()
    native.pViewports = viewports.toNative(scope)
    native.scissorCount = scissorCount.toUInt()
    native.pScissors = scissors.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkPipelineViewportStateCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineViewportStateCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
