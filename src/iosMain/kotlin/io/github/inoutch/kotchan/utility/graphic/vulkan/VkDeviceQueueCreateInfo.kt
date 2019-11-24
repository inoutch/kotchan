package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkDeviceQueueCreateInfo.copyToNative(
    native: vulkan.VkDeviceQueueCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.queueFamilyIndex = queueFamilyIndex.toUInt()
    native.queueCount = queuePriorities.size.toUInt()
    native.pQueuePriorities = queuePriorities.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkDeviceQueueCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkDeviceQueueCreateInfo>().also { copyToNative(it, scope) }.ptr

@ExperimentalUnsignedTypes
fun List<VkDeviceQueueCreateInfo>.toNative(scope: MemScope) =
        scope.allocArray<vulkan.VkDeviceQueueCreateInfo>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } }
