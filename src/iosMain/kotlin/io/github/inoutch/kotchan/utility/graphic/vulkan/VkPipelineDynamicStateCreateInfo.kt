package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_DYNAMIC_STATE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineDynamicStateCreateInfo.copyToNative(
    native: vulkan.VkPipelineDynamicStateCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_DYNAMIC_STATE_CREATE_INFO
    native.pNext = null
    native.dynamicStateCount = dynamicStates.size.toUInt()
    native.pDynamicStates = dynamicStates.map { it.value.toUInt() }.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkPipelineDynamicStateCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineDynamicStateCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
