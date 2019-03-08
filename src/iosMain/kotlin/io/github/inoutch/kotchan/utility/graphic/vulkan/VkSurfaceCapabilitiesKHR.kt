package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr

@ExperimentalUnsignedTypes
fun vulkan.VkSurfaceCapabilitiesKHR.toOrigin(): VkSurfaceCapabilitiesKHR {
    return VkSurfaceCapabilitiesKHR(
            minImageCount.toInt(),
            maxImageCount.toInt(),
            currentExtent.toOrigin(),
            minImageExtent.toOrigin(),
            maxImageExtent.toOrigin(),
            maxImageArrayLayers.toInt(),
            VkSurfaceTransformFlagBitsKHR.values()
                    .filter { it.value.toUInt() and supportedTransforms != 0u },
            VkSurfaceTransformFlagBitsKHR.values()
                    .find { it.value.toUInt() == currentTransform } ?: throw VkNullError("currentTransform"),
            VkCompositeAlphaFlagBitsKHR.values()
                    .filter { it.value.toUInt() and supportedCompositeAlpha != 0u },
            VkImageUsageFlagBits.values()
                    .filter { it.value.toUInt() and supportedUsageFlags != 0u })
}

@ExperimentalUnsignedTypes
actual fun vkGetPhysicalDeviceSurfaceCapabilitiesKHR(physicalDevice: VkPhysicalDevice, surface: VkSurface) = memScoped {
    val native = alloc<vulkan.VkSurfaceCapabilitiesKHR>()

    checkError(vulkan.vkGetPhysicalDeviceSurfaceCapabilitiesKHR(
            physicalDevice.native,
            surface.native,
            native.ptr))

    native.toOrigin()
}
