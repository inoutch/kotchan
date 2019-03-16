package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.Image
import io.github.inoutch.kotchan.utility.graphic.vulkan.*

class VKTexture(val vk: VK, rawImage: Image) : Disposable {

    val image: VkImage

    val imageMemory: VkDeviceMemory

    val imageView: VkImageView

    val sampler: VkSampler

    val size = rawImage.size

    init {
        val format = VkFormat.VK_FORMAT_R8G8B8A8_UNORM
        val imageSize = rawImage.size.x * rawImage.size.y * 4L
        val formatProperties = vkGetPhysicalDeviceFormatProperties(vk.physicalDevice, format)
        val noStaging = formatProperties
                .linearTilingFeatures.contains(VkFormatFeatureFlagBits.VK_FORMAT_FEATURE_SAMPLED_IMAGE_BIT)

        if (!noStaging) {
            // TODO: fix bug for ios
            val stagingBuffer = VKBufferMemory(
                    vk, imageSize, listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_TRANSFER_SRC_BIT),
                    listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                            VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT))

            val data = vkMapMemory(vk.device, stagingBuffer.memory, 0, stagingBuffer.allocateSize, listOf())
            data.copy(0, rawImage.byteArray.size.toLong(), rawImage.byteArray)
            data.dispose()

            image = Helper.createImage(
                    vk.device, rawImage.size, format, VkImageTiling.VK_IMAGE_TILING_OPTIMAL,
                    listOf(VkImageUsageFlagBits.VK_IMAGE_USAGE_TRANSFER_DST_BIT,
                            VkImageUsageFlagBits.VK_IMAGE_USAGE_SAMPLED_BIT))
            val imageMemoryBundle = Helper.createImageMemory(
                    vk.device, image,
                    listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT))
            imageMemory = imageMemoryBundle.first
            vkBindImageMemory(vk.device, image, imageMemory, 0)

            vk.transitionImageLayout(
                    image,
                    VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                    VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL)

            vk.copyImageBuffer(size, stagingBuffer.buffer, image)

            vk.transitionImageLayout(
                    image,
                    VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL,
                    VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL)

            stagingBuffer.dispose()
        } else {
            image = Helper.createImage(
                    vk.device, rawImage.size, format, VkImageTiling.VK_IMAGE_TILING_LINEAR,
                    listOf(VkImageUsageFlagBits.VK_IMAGE_USAGE_SAMPLED_BIT))
            val imageMemoryBundle = Helper.createImageMemory(
                    vk.device, image,
                    listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                            VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT))
            imageMemory = imageMemoryBundle.first
            vkBindImageMemory(vk.device, image, imageMemory, 0)

            val subresourceLayout = vkGetImageSubresourceLayout(vk.device, image,
                    VkImageSubresource(listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT), 0, 0))
            val data = vkMapMemory(vk.device, imageMemory, 0, imageMemoryBundle.second, listOf())
            for (y in 0 until rawImage.size.y) {
                val imageOffset = y * rawImage.size.x * 4
                val row = rawImage.byteArray.sliceArray(IntRange(imageOffset, imageOffset + rawImage.size.x * 4 - 1))
                data.copy(y * subresourceLayout.rowPitch, row.size.toLong(), row)
            }
            data.dispose()

            vk.transitionImageLayout(
                    image,
                    VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                    VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL)
        }

        imageView = Helper.createImageView(vk.device, image, format)
        sampler = Helper.createSampler(vk.device, VkFilter.VK_FILTER_NEAREST, VkFilter.VK_FILTER_NEAREST)
    }

    override fun dispose() {
        sampler.dispose()
        imageView.dispose()
        image.dispose()
        imageMemory.dispose()
    }
}
