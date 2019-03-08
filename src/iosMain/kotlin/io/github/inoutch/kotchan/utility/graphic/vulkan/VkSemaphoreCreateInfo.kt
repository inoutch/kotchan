package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkSemaphoreCreateInfo.copyToNative(native: vulkan.VkSemaphoreCreateInfo) {
    native.sType = VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
}

@ExperimentalUnsignedTypes
fun VkSemaphoreCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkSemaphoreCreateInfo>()
                .also { copyToNative(it) }.ptr
