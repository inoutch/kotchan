package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotlin.vulkan.api.VkDeviceCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkDeviceQueueCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkFormat
import io.github.inoutch.kotlin.vulkan.api.VkFormatFeatureFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkFormatProperties
import io.github.inoutch.kotlin.vulkan.api.VkImageTiling
import io.github.inoutch.kotlin.vulkan.api.VkPhysicalDevice
import io.github.inoutch.kotlin.vulkan.api.VkPhysicalDeviceMemoryProperties
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.VkSurface
import io.github.inoutch.kotlin.vulkan.api.vk
import io.github.inoutch.kotlin.vulkan.utility.DeviceQueueFamilyIndices
import io.github.inoutch.kotlin.vulkan.utility.SwapchainSupportDetails
import io.github.inoutch.kotlin.vulkan.utility.findSupportedFormat

class VKPhysicalDevice(
    val physicalDevice: VkPhysicalDevice,
    val surface: VkSurface
) : Disposer() {
    val physicalDeviceMemoryProperties: VkPhysicalDeviceMemoryProperties by lazy {
        getProperty<VkPhysicalDeviceMemoryProperties> { vk.getPhysicalDeviceMemoryProperties(physicalDevice, it) }
    }

    val deviceQueueFamilyIndices: DeviceQueueFamilyIndices by lazy {
        checkNotNull(DeviceQueueFamilyIndices.find(physicalDevice, surface)) { "Failed to find device queue family indices" }
    }

    val swapchainSupportDetails: SwapchainSupportDetails by lazy {
        checkNotNull(SwapchainSupportDetails.querySwapchainSupport(physicalDevice, surface)) { "Failed to query swapchain support" }
    }

    val depthFormat: VkFormat by lazy {
        checkNotNull(findSupportedFormat(
                physicalDevice,
                listOf(VkFormat.VK_FORMAT_D32_SFLOAT,
                        VkFormat.VK_FORMAT_D32_SFLOAT_S8_UINT,
                        VkFormat.VK_FORMAT_D24_UNORM_S8_UINT),
                VkImageTiling.VK_IMAGE_TILING_OPTIMAL,
                listOf(VkFormatFeatureFlagBits.VK_FORMAT_FEATURE_DEPTH_STENCIL_ATTACHMENT_BIT))
        ) { "Failed to find depth format" }
    }

    fun createDevice(enableDeviceLayerNames: List<String>, enableDeviceExtensionNames: List<String>): VKLogicalDevice {
        val deviceQueueCreateInfos = mutableListOf<VkDeviceQueueCreateInfo>()
        deviceQueueCreateInfos.add(VkDeviceQueueCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO,
                emptyList(),
                deviceQueueFamilyIndices.graphicsQueueFamilyIndex))
        if (deviceQueueFamilyIndices.graphicsQueueFamilyIndex != deviceQueueFamilyIndices.presentQueueFamilyIndex) {
            deviceQueueCreateInfos.add(VkDeviceQueueCreateInfo(
                    VkStructureType.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO,
                    emptyList(),
                    deviceQueueFamilyIndices.presentQueueFamilyIndex))
        }
        val deviceCreateInfo = VkDeviceCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO,
                emptyList(),
                deviceQueueCreateInfos,
                enableDeviceLayerNames,
                enableDeviceExtensionNames,
                null)
        return add(VKLogicalDevice(
                this,
                getProperty { vk.createDevice(physicalDevice, deviceCreateInfo, it).value }
        ))
    }

    fun getPhysicalDeviceFormatProperties(format: VkFormat): VkFormatProperties {
        return getProperty { vk.getPhysicalDeviceFormatProperties(physicalDevice, format, it) }
    }
}
