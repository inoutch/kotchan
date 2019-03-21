package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.graphic.vulkan.*

class DeviceQueueFamilyIndices private constructor(
        val graphicsQueueFamilyIndex: Int,
        val presentQueueFamilyIndex: Int) {
    companion object {
        fun find(physicalDevice: VkPhysicalDevice, surface: VkSurface): DeviceQueueFamilyIndices {
            val deviceQueueFamilyProperties = vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice)
            val graphicsQueueFamilyIndex = deviceQueueFamilyProperties.indexOfFirst {
                it.queueFlags and VkQueueFlagBits.VK_QUEUE_GRAPHICS_BIT.value != 0
            }
            val supportPresents = deviceQueueFamilyProperties.mapIndexed { index, _ ->
                vkGetPhysicalDeviceSurfaceSupportKHR(physicalDevice, index, surface)
            }
            val presentQueueFamilyIndex = List(supportPresents.size) { it }.indexOfFirst {
                supportPresents[it] &&
                        deviceQueueFamilyProperties[it].queueFlags and VkQueueFlagBits.VK_QUEUE_GRAPHICS_BIT.value != 0
            }
            if (graphicsQueueFamilyIndex == -1 || presentQueueFamilyIndex == -1) {
                throw Error("Unsupported graphics queue family of gpu")
            }
            return DeviceQueueFamilyIndices(graphicsQueueFamilyIndex, presentQueueFamilyIndex)
        }
    }
}
