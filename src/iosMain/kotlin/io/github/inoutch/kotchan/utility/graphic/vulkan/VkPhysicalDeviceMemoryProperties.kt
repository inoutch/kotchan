package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun vulkan.VkPhysicalDeviceMemoryProperties.toOrigin(): VkPhysicalDeviceMemoryProperties {
    return VkPhysicalDeviceMemoryProperties(
            List(memoryTypeCount.toInt()) { memoryTypes[it].toOrigin() },
            List(memoryHeapCount.toInt()) { memoryHeaps[it].toOrigin() })
}

@ExperimentalUnsignedTypes
actual fun vkGetPhysicalDeviceMemoryProperties(physicalDevice: VkPhysicalDevice) = memScoped {
    val native = alloc<vulkan.VkPhysicalDeviceMemoryProperties>()

    vulkan.vkGetPhysicalDeviceMemoryProperties(physicalDevice.native, native.ptr)

    native.toOrigin()
}
