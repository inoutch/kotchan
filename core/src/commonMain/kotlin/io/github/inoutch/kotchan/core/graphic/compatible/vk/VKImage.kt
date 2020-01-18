package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotlin.vulkan.api.VkCommandPool
import io.github.inoutch.kotlin.vulkan.api.VkComponentMapping
import io.github.inoutch.kotlin.vulkan.api.VkComponentSwizzle
import io.github.inoutch.kotlin.vulkan.api.VkDeviceMemory
import io.github.inoutch.kotlin.vulkan.api.VkFormat
import io.github.inoutch.kotlin.vulkan.api.VkImage
import io.github.inoutch.kotlin.vulkan.api.VkImageAspectFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout
import io.github.inoutch.kotlin.vulkan.api.VkImageSubresource
import io.github.inoutch.kotlin.vulkan.api.VkImageSubresourceRange
import io.github.inoutch.kotlin.vulkan.api.VkImageViewCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkImageViewType
import io.github.inoutch.kotlin.vulkan.api.VkMemoryPropertyFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkMemoryRequirements
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.VkSubresourceLayout
import io.github.inoutch.kotlin.vulkan.api.vk

class VKImage(
        val logicalDevice: VKLogicalDevice,
        val image: VkImage,
        private val disposable: Boolean = true
) : Disposer() {
    override fun isDisposed(): Boolean {
        return disposable && super.isDisposed()
    }

    val memoryRequirements: VkMemoryRequirements by lazy {
        getProperty<VkMemoryRequirements> { vk.getImageMemoryRequirements(logicalDevice.device, image, it) }
    }

    fun createImageView(
            format: VkFormat,
            aspectFlagBits: List<VkImageAspectFlagBits> = listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT)
    ): VKImageView {
        val createInfo = VkImageViewCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO,
                emptyList(),
                image,
                VkImageViewType.VK_IMAGE_VIEW_TYPE_2D,
                format,
                VkComponentMapping(
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY,
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY,
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY,
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY),
                VkImageSubresourceRange(aspectFlagBits, 0, 1, 0, 1))
        return VKImageView(getProperty { vk.createImageView(logicalDevice.device, createInfo, it).value })
                .also { add(it) }
    }

    fun createDepthImageView(): VKImageView {
        return createImageView(logicalDevice.physicalDevice.depthFormat, listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_DEPTH_BIT))
    }

    fun bindImageMemory(deviceMemory: VKDeviceMemory, memoryOffset: Long) {
        vk.bindImageMemory(logicalDevice.device, image, deviceMemory.deviceMemory, 0)
    }

    fun allocateImageDeviceMemory(
            memoryProperties: List<VkMemoryPropertyFlagBits>
    ): VKImageDeviceMemory {
        val physicalDeviceMemoryProperties = logicalDevice.physicalDevice.physicalDeviceMemoryProperties
        val memoryTypeIndex = physicalDeviceMemoryProperties.findMemoryTypeIndex(
                memoryRequirements,
                memoryProperties
        )
        checkNotNull(memoryTypeIndex) { "Failed to find memory type index" }
        val imageDeviceMemory = logicalDevice.allocateDeviceMemory(memoryRequirements.size, memoryTypeIndex)
        logicalDevice.remove(imageDeviceMemory)

        bindImageMemory(imageDeviceMemory, 0)
        return VKImageDeviceMemory(this, imageDeviceMemory)
                .also { add(it) }
    }

    fun allocateDepthImageDeviceMemory(commandPool: VKCommandPool): VKImageDeviceMemory {
        val depthImageMemory = allocateImageDeviceMemory(listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT))
        commandPool.transitionImageLayout(
                this,
                logicalDevice.physicalDevice.depthFormat,
                VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                VkImageLayout.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL
        )

        return VKImageDeviceMemory(this, depthImageMemory.deviceMemory)
    }

    fun getImageSubresourceLayout(subresource: VkImageSubresource): VkSubresourceLayout {
        return getProperty { vk.getImageSubresourceLayout(logicalDevice.device, image, subresource, it) }
    }
}
