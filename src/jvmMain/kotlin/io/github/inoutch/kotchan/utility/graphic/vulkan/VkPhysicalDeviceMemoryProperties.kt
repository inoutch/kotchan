package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

fun org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties.toOrigin(): VkPhysicalDeviceMemoryProperties {
    return VkPhysicalDeviceMemoryProperties(
            memoryTypes().map { VkMemoryType(it.heapIndex(), it.propertyFlags()) },
            memoryHeaps().map { VkMemoryHeap(it.size(), it.flags()) })
}

actual fun vkGetPhysicalDeviceMemoryProperties(physicalDevice: VkPhysicalDevice) = memScoped {
    val native = add(org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties.calloc())

    VK10.vkGetPhysicalDeviceMemoryProperties(physicalDevice.native, native)

    native.toOrigin()
}
