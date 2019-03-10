package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_FENCE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkFenceCreateInfo.copyToNative(native: vulkan.VkFenceCreateInfo) {
    native.sType = VK_STRUCTURE_TYPE_FENCE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
}

@ExperimentalUnsignedTypes
fun VkFenceCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkFenceCreateInfo>()
                .also { copyToNative(it) }.ptr
