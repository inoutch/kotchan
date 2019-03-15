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
        val imageSize = rawImage.size.x * rawImage.size.y * 4L
        val stagingBuffer = VKBufferMemory(
                vk,
                imageSize,
                listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_TRANSFER_SRC_BIT),
                listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT))

        val data = vkMapMemory(vk.device, stagingBuffer.memory, 0, imageSize, listOf())
        data.copy(0, rawImage.byteArray.size.toLong(), rawImage.byteArray)
        data.dispose()

        image = Helper.createImage(
                vk.device,
                rawImage.size,
                VkFormat.VK_FORMAT_R8G8B8A8_UNORM,
                VkImageTiling.VK_IMAGE_TILING_OPTIMAL,
                listOf(VkImageUsageFlagBits.VK_IMAGE_USAGE_TRANSFER_DST_BIT,
                        VkImageUsageFlagBits.VK_IMAGE_USAGE_SAMPLED_BIT))
        imageMemory = Helper.createMemory(
                vk.device, image,
                listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT))

        vkBindImageMemory(vk.device, image, imageMemory, 0)

        vk.transitionImageLayout(
                image,
                VkFormat.VK_FORMAT_R8G8B8A8_UNORM,
                VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL)

        vk.copyImageBuffer(size, stagingBuffer.buffer, image)

        vk.transitionImageLayout(
                image,
                VkFormat.VK_FORMAT_R8G8B8A8_UNORM,
                VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL,
                VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL)

        stagingBuffer.dispose()

        imageView = Helper.createImageView(vk.device, image, VkFormat.VK_FORMAT_R8G8B8A8_UNORM)

        sampler = Helper.createSampler(vk.device, VkFilter.VK_FILTER_NEAREST, VkFilter.VK_FILTER_NEAREST)
    }

    override fun dispose() {
        sampler.dispose()
        imageView.dispose()
        image.dispose()
        imageMemory.dispose()
    }
}
