package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.KHRSurface

fun org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR.toOrigin() =
        VkSurfaceCapabilitiesKHR(
                minImageCount(),
                maxImageCount(),
                currentExtent().toOrigin(),
                minImageExtent().toOrigin(),
                maxImageExtent().toOrigin(),
                maxImageArrayLayers(),
                VkSurfaceTransformFlagBitsKHR.values().filter { it.value and supportedTransforms() != 0 },
                VkSurfaceTransformFlagBitsKHR.values().find { it.value == currentTransform() }
                        ?: throw VkNullError("surfaceCapabilities.currentTransform"),
                VkCompositeAlphaFlagBitsKHR.values().filter { it.value and supportedCompositeAlpha() != 0 },
                VkImageUsageFlagBits.values().filter { it.value and supportedUsageFlags() != 0 })

actual fun vkGetPhysicalDeviceSurfaceCapabilitiesKHR(
        physicalDevice: VkPhysicalDevice,
        surface: VkSurface) = memScoped {
    val native = add(org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR.calloc())

    checkError(KHRSurface.vkGetPhysicalDeviceSurfaceCapabilitiesKHR(
            physicalDevice.native,
            surface.native,
            native))

    native.toOrigin()
}
