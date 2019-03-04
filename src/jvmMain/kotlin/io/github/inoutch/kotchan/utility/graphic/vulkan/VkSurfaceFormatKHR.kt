package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.KHRSurface

fun org.lwjgl.vulkan.VkSurfaceFormatKHR.toOrigin() =
        VkSurfaceFormatKHR(
                VkFormat.values().find { it.value == format() } ?: throw VkNullError("surface.format"),
                VkColorSpaceKHR.values().find { it.value == colorSpace() } ?: throw VkNullError("surface.colorSpace"))

actual fun vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice: VkPhysicalDevice, surface: VkSurface) = memScoped {
    val count = allocInt()

    checkError(KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice.native, surface.native, count, null))

    val surfaceFormats = add(org.lwjgl.vulkan.VkSurfaceFormatKHR.calloc(count.get(0)))
    checkError(KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice.native, surface.native, count, surfaceFormats))

    List(count.get(0)) { surfaceFormats[it].toOrigin() }
}
