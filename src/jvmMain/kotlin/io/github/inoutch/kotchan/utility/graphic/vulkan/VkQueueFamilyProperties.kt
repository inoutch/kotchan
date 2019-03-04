package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

fun org.lwjgl.vulkan.VkQueueFamilyProperties.toOrigin(): VkQueueFamilyProperties {
    return VkQueueFamilyProperties(queueFlags(), queueCount(), timestampValidBits(),
            minImageTransferGranularity().let { VkExtend3D(it.width(), it.height(), it.depth()) })
}

actual fun vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice: VkPhysicalDevice) = memScoped {
    val queueCount = allocInt()
    VK10.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice.native, queueCount, null)

    val native = add(org.lwjgl.vulkan.VkQueueFamilyProperties.calloc(queueCount.get(0)))
    VK10.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice.native, queueCount, native)

    List(queueCount.get(0)) { native[it].toOrigin() }
}
