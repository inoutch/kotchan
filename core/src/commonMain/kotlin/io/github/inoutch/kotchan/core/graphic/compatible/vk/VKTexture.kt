package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.extension.toVKExtent2D
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.vulkan.api.VkBufferUsageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkFormat
import io.github.inoutch.kotlin.vulkan.api.VkFormatFeatureFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkImageAspectFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout
import io.github.inoutch.kotlin.vulkan.api.VkImageSubresource
import io.github.inoutch.kotlin.vulkan.api.VkImageTiling
import io.github.inoutch.kotlin.vulkan.api.VkImageUsageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkMemoryPropertyFlagBits

class VKTexture(
    logicalDevice: VKLogicalDevice,
    commandPool: VKCommandPool,
    imageRaw: Image
) : Texture() {

    override val size: Vector2I = imageRaw.size

    val image: VKImage

    val imageDeviceMemory: VKImageDeviceMemory

    val imageView: VKImageView

    val sampler: VKSampler

    init {
        try {
            // TODO: Check image format
            val format = VkFormat.VK_FORMAT_R8G8B8A8_UNORM
            val colorBytes = 4
            val imageSize = imageRaw.size.x * imageRaw.size.y * colorBytes.toLong()
            val formatProperties = logicalDevice.physicalDevice.getPhysicalDeviceFormatProperties(format)
            val noStaging = formatProperties
                    .linearTilingFeatures
                    .contains(VkFormatFeatureFlagBits.VK_FORMAT_FEATURE_SAMPLED_IMAGE_BIT)

            if (!noStaging) {
                // Use staging with transition image layout
                val buffer = logicalDevice.createBuffer(imageSize, listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_TRANSFER_SRC_BIT))
                add(buffer)

                val bufferDeviceMemory = buffer.allocateBufferDeviceMemory()
                add(bufferDeviceMemory)

                val data = bufferDeviceMemory.mapMemory()
                data.copy(0L, imageRaw.byteArray.size.toLong(), imageRaw.byteArray)
                data.destroy()

                image = logicalDevice.createImage(
                        imageRaw.size.toVKExtent2D(),
                        format,
                        VkImageTiling.VK_IMAGE_TILING_OPTIMAL,
                        listOf(VkImageUsageFlagBits.VK_IMAGE_USAGE_TRANSFER_DST_BIT,
                                VkImageUsageFlagBits.VK_IMAGE_USAGE_SAMPLED_BIT)
                )
                imageDeviceMemory = image.allocateImageDeviceMemory(
                        listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT)
                )

                commandPool.transitionImageLayout(
                        image,
                        format,
                        VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                        VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL
                )
                commandPool.copyBufferToImage(imageRaw.size, buffer, image)
                commandPool.transitionImageLayout(
                        image,
                        format,
                        VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL,
                        VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL
                )

                dispose(bufferDeviceMemory)
                dispose(buffer)
            } else {
                image = logicalDevice.createImage(
                        imageRaw.size.toVKExtent2D(),
                        format,
                        VkImageTiling.VK_IMAGE_TILING_LINEAR,
                        listOf(VkImageUsageFlagBits.VK_IMAGE_USAGE_SAMPLED_BIT)
                )
                add(image)

                imageDeviceMemory = image.allocateImageDeviceMemory(
                        listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                                VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)
                )
                add(imageDeviceMemory)
                val imageSubresource = VkImageSubresource(listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT), 0, 0)
                val imageSubresourceLayout = image.getImageSubresourceLayout(imageSubresource)

                val data = imageDeviceMemory.mapMemory()
                for (y in 0 until size.y) {
                    val imageOffset = y * size.x * colorBytes
                    val row = imageRaw.byteArray.sliceArray(IntRange(imageOffset, imageOffset + imageRaw.size.x * colorBytes - 1))
                    data.copy(y * imageSubresourceLayout.rowPitch, row.size.toLong(), row)
                }
                data.destroy()

                commandPool.transitionImageLayout(
                        image,
                        format,
                        VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                        VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL
                )
            }

            imageView = image.createImageView(format)
            add(imageView)

            sampler = logicalDevice.createSampler(
                    magFilter.vkParam,
                    minFilter.vkParam,
                    mipmapMode.vkParam,
                    addressModeU.vkParam,
                    addressModeV.vkParam
            )
            add(sampler)
        } catch (e: Error) {
            dispose()
            throw e
        }
    }
}
