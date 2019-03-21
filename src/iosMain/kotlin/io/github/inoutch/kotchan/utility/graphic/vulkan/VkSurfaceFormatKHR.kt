package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun vulkan.VkSurfaceFormatKHR.toOrigin(): VkSurfaceFormatKHR {
    return VkSurfaceFormatKHR(VkFormat.values().find { it.value.toUInt() == format }
            ?: throw VkNullError("format"),
            VkColorSpaceKHR.values().find { it.value.toUInt() == colorSpace }
                    ?: throw VkNullError("colorSpace"))
}

@ExperimentalUnsignedTypes
actual fun vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice: VkPhysicalDevice, surface: VkSurface) = memScoped {
    val count = alloc<UIntVar>()

    checkError(vulkan.vkGetPhysicalDeviceSurfaceFormatsKHR(
            physicalDevice.native,
            surface.native,
            count.ptr,
            null))

    val natives = allocArray<vulkan.VkSurfaceFormatKHR>(count.value.toInt())

    checkError(vulkan.vkGetPhysicalDeviceSurfaceFormatsKHR(
            physicalDevice.native,
            surface.native,
            count.ptr,
            natives))

    List(count.value.toInt()) { natives[it].toOrigin() }
}
