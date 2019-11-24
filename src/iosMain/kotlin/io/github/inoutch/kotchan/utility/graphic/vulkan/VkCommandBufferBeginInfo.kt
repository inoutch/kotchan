package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO

@ExperimentalUnsignedTypes
fun VkCommandBufferBeginInfo.copyToNative(
    native: vulkan.VkCommandBufferBeginInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO
    native.pNext = null
    native.flags = flags.sumBy { it.value }.toUInt()
    native.pInheritanceInfo = inheritanceInfo?.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkCommandBufferBeginInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkCommandBufferBeginInfo>().also { copyToNative(it, scope) }.ptr
