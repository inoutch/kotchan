package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkCommandPoolCreateInfo.copyToNative(native: vulkan.VkCommandPoolCreateInfo) {
    native.sType = VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.queueFamilyIndex = queueFamilyIndex.toUInt()
}

@ExperimentalUnsignedTypes
fun VkCommandPoolCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkCommandPoolCreateInfo>()
                .also { copyToNative(it) }.ptr
