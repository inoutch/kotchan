package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkDeviceCreateInfo.copyToNative(
    native: vulkan.VkDeviceCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.queueCreateInfoCount = queueCreateInfos.size.toUInt()
    native.pQueueCreateInfos = queueCreateInfos.toNative(scope)
    native.enabledLayerCount = enabledLayerNames.size.toUInt()
    native.ppEnabledLayerNames = enabledLayerNames.toCStringArray(scope)
    native.enabledExtensionCount = enabledExtensionNames.size.toUInt()
    native.ppEnabledExtensionNames = enabledExtensionNames.toCStringArray(scope)
    native.pEnabledFeatures = enabledFeatures?.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkDeviceCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkDeviceCreateInfo>().also { copyToNative(it, scope) }.ptr
