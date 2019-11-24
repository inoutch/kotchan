package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineLayoutCreateInfo.copyToNative(
    native: vulkan.VkPipelineLayoutCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.setLayoutCount = setLayouts.size.toUInt()
    native.pSetLayouts = setLayouts.toNative(scope)
    native.pushConstantRangeCount = pushConstantRanges.size.toUInt()
    native.pPushConstantRanges = pushConstantRanges.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkPipelineLayoutCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineLayoutCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
