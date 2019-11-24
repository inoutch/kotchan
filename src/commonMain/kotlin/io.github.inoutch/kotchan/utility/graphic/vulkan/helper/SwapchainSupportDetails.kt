package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.graphic.vulkan.*
import io.github.inoutch.kotchan.utility.type.Point
import kotlin.math.max
import kotlin.math.min

class SwapchainSupportDetails private constructor(
    val capabilities: VkSurfaceCapabilitiesKHR,
    val formats: List<VkSurfaceFormatKHR>,
    val presentModes: List<VkPresentModeKHR>
) {

    companion object {
        fun querySwapchainSupport(physicalDevice: VkPhysicalDevice, surface: VkSurface): SwapchainSupportDetails {
            val capabilities = vkGetPhysicalDeviceSurfaceCapabilitiesKHR(physicalDevice, surface)
            val formats = vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice, surface)
            val presentModes = vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice, surface)
            return SwapchainSupportDetails(capabilities, formats, presentModes)
        }
    }

    fun chooseSwapSurfaceFormat(): VkSurfaceFormatKHR {
        if (formats.size == 1 && formats.first().format == VkFormat.VK_FORMAT_UNDEFINED) {
            return VkSurfaceFormatKHR(VkFormat.VK_FORMAT_B8G8R8A8_UNORM, VkColorSpaceKHR.VK_COLOR_SPACE_SRGB_NONLINEAR_KHR)
        }

        formats.find { it.format == VkFormat.VK_FORMAT_B8G8R8A8_UNORM && it.colorSpace == VkColorSpaceKHR.VK_COLOR_SPACE_SRGB_NONLINEAR_KHR }

        return formats.first()
    }

    fun chooseSwapPresentMode(): VkPresentModeKHR {
        var swapchainPresentMode = VkPresentModeKHR.VK_PRESENT_MODE_FIFO_KHR
        for (presentMode in presentModes) {
            if (presentMode == VkPresentModeKHR.VK_PRESENT_MODE_MAILBOX_KHR) {
                return VkPresentModeKHR.VK_PRESENT_MODE_MAILBOX_KHR
            }
            if (presentMode == VkPresentModeKHR.VK_PRESENT_MODE_IMMEDIATE_KHR) {
                swapchainPresentMode = VkPresentModeKHR.VK_PRESENT_MODE_IMMEDIATE_KHR
            }
        }
        return swapchainPresentMode
    }

    fun chooseSwapExtent(actualWindowSize: Point): Point {
        if (capabilities.currentExtent.x != -1) {
            return capabilities.currentExtent
        }

        return Point(
                max(capabilities.minImageExtent.x, min(capabilities.maxImageExtent.x, actualWindowSize.x)),
                max(capabilities.minImageExtent.y, min(capabilities.maxImageExtent.y, actualWindowSize.y)))
    }

    fun chooseImageCount(): Int {
        var imageCount = capabilities.minImageCount + 1
        if (capabilities.maxImageCount in 1..(imageCount - 1)) {
            imageCount = capabilities.maxImageCount
        }
        return imageCount
    }

    fun chooseTransform(): VkSurfaceTransformFlagBitsKHR {
        return if (capabilities.supportedTransforms.contains(VkSurfaceTransformFlagBitsKHR.VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR)) {
            VkSurfaceTransformFlagBitsKHR.VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR
        } else {
            capabilities.currentTransform
        }
    }
}
