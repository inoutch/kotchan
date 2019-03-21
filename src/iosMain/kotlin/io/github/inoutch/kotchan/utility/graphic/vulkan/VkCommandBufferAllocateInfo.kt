package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO

@ExperimentalUnsignedTypes
fun VkCommandBufferAllocateInfo.copyToNative(native: vulkan.VkCommandBufferAllocateInfo) {
    native.sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO
    native.pNext = null
    native.commandPool = commandPool.native
    native.commandBufferCount = commandBufferCount.toUInt()
    native.level = level.toUInt()
}

@ExperimentalUnsignedTypes
fun VkCommandBufferAllocateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkCommandBufferAllocateInfo>().also { copyToNative(it) }.ptr
