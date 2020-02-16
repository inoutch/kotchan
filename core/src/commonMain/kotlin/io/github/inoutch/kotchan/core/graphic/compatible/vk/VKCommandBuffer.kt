package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VK_QUEUE_FAMILY_IGNORED
import io.github.inoutch.kotlin.vulkan.api.VkAccessFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkClearColorValue
import io.github.inoutch.kotlin.vulkan.api.VkClearDepthStencilValue
import io.github.inoutch.kotlin.vulkan.api.VkClearValue
import io.github.inoutch.kotlin.vulkan.api.VkCommandBuffer
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferBeginInfo
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferResetFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkFormat
import io.github.inoutch.kotlin.vulkan.api.VkImageAspectFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout
import io.github.inoutch.kotlin.vulkan.api.VkImageMemoryBarrier
import io.github.inoutch.kotlin.vulkan.api.VkImageSubresourceRange
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits.VK_PIPELINE_STAGE_BOTTOM_OF_PIPE_BIT
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TRANSFER_BIT
import io.github.inoutch.kotlin.vulkan.api.VkRect2D
import io.github.inoutch.kotlin.vulkan.api.VkRenderPassBeginInfo
import io.github.inoutch.kotlin.vulkan.api.VkResult
import io.github.inoutch.kotlin.vulkan.api.VkStructureType.VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER
import io.github.inoutch.kotlin.vulkan.api.VkStructureType.VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO
import io.github.inoutch.kotlin.vulkan.api.VkStructureType.VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO
import io.github.inoutch.kotlin.vulkan.api.VkSubpassContents
import io.github.inoutch.kotlin.vulkan.api.VkViewport
import io.github.inoutch.kotlin.vulkan.api.vk
import io.github.inoutch.kotlin.vulkan.utility.SingleCommandBuffer
import io.github.inoutch.kotlin.vulkan.utility.hasStencilComponent

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

    fun cmdBeginRenderPass(
            renderPass: VKRenderPass,
            framebuffer: VKFramebuffer,
            renderArea: VkRect2D,
            clearValues: List<VkClearValue>
    ) {
        val renderPassBeginInfo = VkRenderPassBeginInfo(
                VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO,
                renderPass.renderPass,
                framebuffer.framebuffer,
                renderArea,
                clearValues
        )
        vk.cmdBeginRenderPass(commandBuffer, renderPassBeginInfo, VkSubpassContents.VK_SUBPASS_CONTENTS_INLINE)
    }

    fun cmdEndRenderPass() {
        vk.cmdEndRenderPass(commandBuffer)
    }

    fun cmdBindVertexBuffers(vertexBuffers: List<VKVertexBuffer>) {
        vk.cmdBindVertexBuffers(
                commandBuffer,
                0,
                vertexBuffers.map { it.buffer.buffer },
                vertexBuffers.map { 0L }
        )
    }

    fun cmdDraw(vertexCount: Int, instanceCount: Int, firstVertex: Int, firstInstance: Int) {
        vk.cmdDraw(commandBuffer, vertexCount, instanceCount, firstVertex, firstInstance)
    }

    fun transitionImageLayout(
            image: VKImage,
            oldLayout: VkImageLayout,
            newLayout: VkImageLayout
    ) {
        val srcAccessMask: List<VkAccessFlagBits> = listOf()
        val dstAccessMask: List<VkAccessFlagBits> = listOf()
        val sourceStage: List<VkPipelineStageFlagBits> = listOf(VK_PIPELINE_STAGE_TRANSFER_BIT)
        val destinationStage: List<VkPipelineStageFlagBits> = listOf(VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT)
        val aspectMask = mutableListOf<VkImageAspectFlagBits>()
        aspectMask.add(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT)

        val barrier = VkImageMemoryBarrier(
                VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER,
                srcAccessMask,
                dstAccessMask,
                oldLayout,
                newLayout,
                VK_QUEUE_FAMILY_IGNORED,
                VK_QUEUE_FAMILY_IGNORED,
                image.image,
                VkImageSubresourceRange(aspectMask, 0, 1, 0, 1)
        )

        vk.cmdPipelineBarrier(
                commandBuffer,
                sourceStage,
                destinationStage,
                listOf(),
                listOf(),
                listOf(),
                listOf(barrier)
        )
    }
}
