package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_TESSELLATION_STATE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineTessellationStateCreateInfo.copyToNative(native: vulkan.VkPipelineTessellationStateCreateInfo) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_TESSELLATION_STATE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.patchControlPoints = patchControlPoints.toUInt()
}

@ExperimentalUnsignedTypes
fun VkPipelineTessellationStateCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineTessellationStateCreateInfo>()
                .also { copyToNative(it) }.ptr
