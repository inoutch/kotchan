package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkBufferCreateInfo.copyToNative(
    native: vulkan.VkBufferCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.size = size.toULong()
    native.usage = usage.sumBy { it.value }.toUInt()
    native.sharingMode = sharingMode.value.toUInt()
    native.queueFamilyIndexCount = queueFamilyIndices?.size?.toUInt() ?: 0u
    native.pQueueFamilyIndices = queueFamilyIndices?.map { it.toUInt() }?.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkBufferCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkBufferCreateInfo>().also { copyToNative(it, scope) }.ptr
