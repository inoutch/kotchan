package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.KHRSurface
import org.lwjgl.vulkan.VK10.VK_TRUE

actual class VkSurface {
    var native: Long = 0
        private set

    fun init(nativeSurface: Long) {
        this.native = nativeSurface
    }
}

actual fun vkGetPhysicalDeviceSurfaceSupportKHR(
    physicalDevice: VkPhysicalDevice,
    queueFamilyIndex: Int,
    surface: VkSurface
) = memScoped {
    val supportedPresentation = allocInt(1)

    checkError(KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(
            physicalDevice.native, queueFamilyIndex, surface.native, supportedPresentation))

    supportedPresentation.get(0) == VK_TRUE
}

actual fun vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice: VkPhysicalDevice, surface: VkSurface) = memScoped {
    val count = allocInt()

    checkError(KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(
            physicalDevice.native, surface.native, count, null))

    val presentModes = allocInt(count.get(0))

    checkError(KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(
            physicalDevice.native, surface.native, count, null))

    List(count.get(0)) { i -> VkPresentModeKHR.values().find { it.value == presentModes[i] } }.filterNotNull()
}
