package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkInstanceCreateInfo.copyToNative(
    native: vulkan.VkInstanceCreateInfo,
    scope: MemScope
) {
    native.sType = vulkan.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.enabledExtensionCount = enabledExtensionNames.size.toUInt()
    native.ppEnabledExtensionNames = enabledExtensionNames.toCStringArray(scope)
    native.enabledLayerCount = enabledLayerNames.size.toUInt()
    native.ppEnabledLayerNames = enabledLayerNames.toCStringArray(scope)
    native.pApplicationInfo = applicationInfo.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkInstanceCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkInstanceCreateInfo>().also { copyToNative(it, scope) }.ptr
