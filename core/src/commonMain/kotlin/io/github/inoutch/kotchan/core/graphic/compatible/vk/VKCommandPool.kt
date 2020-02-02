package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.extension.getProperties
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.vulkan.api.VK_QUEUE_FAMILY_IGNORED
import io.github.inoutch.kotlin.vulkan.api.VkAccessFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkBufferCopy
import io.github.inoutch.kotlin.vulkan.api.VkBufferImageCopy
import io.github.inoutch.kotlin.vulkan.api.VkCommandBuffer
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferAllocateInfo
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferLevel
import io.github.inoutch.kotlin.vulkan.api.VkCommandPool
import io.github.inoutch.kotlin.vulkan.api.VkExtent3D
import io.github.inoutch.kotlin.vulkan.api.VkFormat
import io.github.inoutch.kotlin.vulkan.api.VkImageAspectFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout
import io.github.inoutch.kotlin.vulkan.api.VkImageMemoryBarrier
import io.github.inoutch.kotlin.vulkan.api.VkImageSubresourceLayers
import io.github.inoutch.kotlin.vulkan.api.VkImageSubresourceRange
import io.github.inoutch.kotlin.vulkan.api.VkOffset3D
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.vk
import io.github.inoutch.kotlin.vulkan.utility.SingleCommandBuffer
import io.github.inoutch.kotlin.vulkan.utility.hasStencilComponent

class VKCommandPool(
        val logicalDevice: VKLogicalDevice,
        val queue: VKQueue,
        val commandPool: VkCommandPool
) : Disposer() {
    fun allocateCommandBuffer(size: Int): List<VKCommandBuffer> {
        val allocateInfo = VkCommandBufferAllocateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO,
                commandPool,
                VkCommandBufferLevel.VK_COMMAND_BUFFER_LEVEL_PRIMARY,
                size
        )
        return getProperties<VkCommandBuffer> { vk.allocateCommandBuffers(logicalDevice.device, allocateInfo, it) }
                .map { raw -> VKCommandBuffer(this, raw).also { add(it) } }
    }

    fun transitionImageLayout(image: VKImage, format: VkFormat, oldLayout: VkImageLayout, newLayout: VkImageLayout) {
        val srcAccessMask: List<VkAccessFlagBits>
        val dstAccessMask: List<VkAccessFlagBits>
        val sourceStage: List<VkPipelineStageFlagBits>
        val destinationStage: List<VkPipelineStageFlagBits>
        val aspectMask = mutableListOf<VkImageAspectFlagBits>()

        if (newLayout == VkImageLayout.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL) {
            aspectMask.add(VkImageAspectFlagBits.VK_IMAGE_ASPECT_DEPTH_BIT)
            if (format.hasStencilComponent()) {
                aspectMask.add(VkImageAspectFlagBits.VK_IMAGE_ASPECT_STENCIL_BIT)
            }
        } else {
            aspectMask.add(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT)
        }

        if (oldLayout == VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED &&
                newLayout == VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL) {
            srcAccessMask = listOf()
            dstAccessMask = listOf(VkAccessFlagBits.VK_ACCESS_TRANSFER_WRITE_BIT)

            sourceStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT)
            destinationStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TRANSFER_BIT)
        } else if (oldLayout == VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL &&
                newLayout == VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL) {
            srcAccessMask = listOf(VkAccessFlagBits.VK_ACCESS_TRANSFER_WRITE_BIT)
            dstAccessMask = listOf(VkAccessFlagBits.VK_ACCESS_SHADER_READ_BIT)

            sourceStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TRANSFER_BIT)
            destinationStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT)
        } else if (oldLayout == VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED &&
                newLayout == VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL) {
            srcAccessMask = listOf()
            dstAccessMask = listOf(VkAccessFlagBits.VK_ACCESS_SHADER_READ_BIT)

            sourceStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT)
            destinationStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT)
        } else if (oldLayout == VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED &&
                newLayout == VkImageLayout.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL) {
            srcAccessMask = listOf()
            dstAccessMask = listOf(
                    VkAccessFlagBits.VK_ACCESS_DEPTH_STENCIL_ATTACHMENT_READ_BIT,
                    VkAccessFlagBits.VK_ACCESS_DEPTH_STENCIL_ATTACHMENT_WRITE_BIT)

            sourceStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT)
            destinationStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_EARLY_FRAGMENT_TESTS_BIT)
        } else {
            throw IllegalStateException("Unsupported layout transition")
        }

        val barrier = VkImageMemoryBarrier(
                VkStructureType.VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER,
                srcAccessMask,
                dstAccessMask,
                oldLayout,
                newLayout,
                VK_QUEUE_FAMILY_IGNORED,
                VK_QUEUE_FAMILY_IGNORED,
                image.image,
                VkImageSubresourceRange(aspectMask, 0, 1, 0, 1)
        )

        val singleCommandBuffer = SingleCommandBuffer(logicalDevice.device, commandPool, queue.queue)
        singleCommandBuffer.submit {
            vk.cmdPipelineBarrier(
                    it,
                    sourceStage,
                    destinationStage,
                    listOf(),
                    listOf(),
                    listOf(),
                    listOf(barrier)
            )
        }
        singleCommandBuffer.destroy()
    }

    fun copyBufferToImage(size: Vector2I, buffer: VKBuffer, image: VKImage) {
        val region = VkBufferImageCopy(
                0, 0, 0,
                VkImageSubresourceLayers(listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT), 0, 0, 1),
                VkOffset3D(0, 0, 0),
                VkExtent3D(size.x, size.y, 1))
        val singleCommandBuffer = SingleCommandBuffer(logicalDevice.device, commandPool, queue.queue)
        singleCommandBuffer.submit {
            vk.cmdCopyBufferToImage(it, buffer.buffer, image.image, VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL, listOf(region))
        }
        singleCommandBuffer.destroy()
    }

    fun copyBuffer(srcBuffer: VKBuffer, srcOffset: Long, dstBuffer: VKBuffer, dstOffset: Long, size: Long) {
        val region = VkBufferCopy(
                srcOffset,
                dstOffset,
                size
        )
        val singleCommandBuffer = SingleCommandBuffer(logicalDevice.device, commandPool, queue.queue)
        singleCommandBuffer.submit {
            vk.cmdCopyBuffer(it, srcBuffer.buffer, dstBuffer.buffer, listOf(region))
        }
        singleCommandBuffer.destroy()
    }
}
