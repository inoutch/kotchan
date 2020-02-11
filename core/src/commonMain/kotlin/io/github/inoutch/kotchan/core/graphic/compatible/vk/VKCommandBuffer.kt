package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkClearColorValue
import io.github.inoutch.kotlin.vulkan.api.VkClearDepthStencilValue
import io.github.inoutch.kotlin.vulkan.api.VkCommandBuffer
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferBeginInfo
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferResetFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout
import io.github.inoutch.kotlin.vulkan.api.VkImageSubresourceRange
import io.github.inoutch.kotlin.vulkan.api.VkRect2D
import io.github.inoutch.kotlin.vulkan.api.VkResult
import io.github.inoutch.kotlin.vulkan.api.VkViewport
import io.github.inoutch.kotlin.vulkan.api.vk

class VKCommandBuffer(val commandPool: VKCommandPool, val commandBuffer: VkCommandBuffer) : Disposer() {
    init {
        add { vk.freeCommandBuffers(commandPool.logicalDevice.device, commandPool.commandPool, listOf(commandBuffer)) }
    }

    fun resetCommandBuffer(flags: List<VkCommandBufferResetFlagBits> = listOf()) {
        vk.resetCommandBuffer(commandBuffer, flags)
    }

    fun beginCommandBuffer(beginInfo: VkCommandBufferBeginInfo): VkResult {
        return vk.beginCommandBuffer(commandBuffer, beginInfo)
    }

    fun endCommandBuffer(): VkResult {
        return vk.endCommandBuffer(commandBuffer)
    }

    fun cmdSetViewport(viewport: VkViewport) {
        vk.cmdSetViewport(commandBuffer, 0, listOf(viewport))
    }

    fun cmdSetScissor(scissor: VkRect2D) {
        vk.cmdSetScissor(commandBuffer, 0, listOf(scissor))
    }

    fun cmdClearColorImage(
        image: VKImage,
        clearColor: VkClearColorValue,
        ranges: List<VkImageSubresourceRange>
    ) {
        vk.cmdClearColorImage(
                commandBuffer,
                image.image,
                VkImageLayout.VK_IMAGE_LAYOUT_GENERAL,
                clearColor,
                ranges
        )
    }

    fun cmdClearDepthStencilImage(
        image: VKImage,
        depthStencilValue: VkClearDepthStencilValue,
        ranges: List<VkImageSubresourceRange>
    ) {
        vk.cmdClearDepthStencilImage(
                commandBuffer,
                image.image,
                VkImageLayout.VK_IMAGE_LAYOUT_GENERAL,
                depthStencilValue,
                ranges
        )
    }
}
