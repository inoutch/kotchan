package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_TRUE
import vulkan.VkPresentModeKHRVar
import vulkan.VkSurfaceKHR
import vulkan.VkSurfaceKHRVar

actual class VkSurface {
    lateinit var native: VkSurfaceKHR

    fun init(nativeSurface: VkSurfaceKHR) {
        this.native = nativeSurface
    }
}

@ExperimentalUnsignedTypes
actual fun vkGetPhysicalDeviceSurfaceSupportKHR(
        physicalDevice: VkPhysicalDevice,
        queueFamilyIndex: Int,
        surface: VkSurface) = memScoped {
    val supportedPresentation = alloc<UIntVar>()

    vulkan.vkGetPhysicalDeviceSurfaceSupportKHR(
            physicalDevice.native,
            queueFamilyIndex.toUInt(),
            surface.native,
            supportedPresentation.ptr)
    supportedPresentation.value == VK_TRUE.toUInt()
}

@ExperimentalUnsignedTypes
actual fun vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice: VkPhysicalDevice, surface: VkSurface) = memScoped {
    val count = alloc<UIntVar>()

    vulkan.vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice.native, surface.native, count.ptr, null)

    val natives = allocArray<VkPresentModeKHRVar>(count.value.toInt())

    vulkan.vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice.native, surface.native, count.ptr, natives)

    List(count.value.toInt()) { i -> VkPresentModeKHR.values().find { it.value.toUInt() == natives[i] } }
            .filterNotNull()
}

fun vkCreateIOSSurfaceMVK(instance: VkInstance, createInfo: vulkan.VkIOSSurfaceCreateInfoMVK) = memScoped {
    val native = alloc<VkSurfaceKHRVar>()

    checkError(vulkan.vkCreateIOSSurfaceMVK(instance.native, createInfo.ptr, null, native.ptr))

    VkSurface().apply { init(native.value ?: throw VkNullError("surface")) }
}
