package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.type.Vector4
import kotlinx.cinterop.*
import vulkan.VkCommandBufferVar

@ExperimentalUnsignedTypes
actual class VkCommandBuffer : Disposable {
    lateinit var native: vulkan.VkCommandBuffer
        private set

    private lateinit var device: VkDevice

    private lateinit var commandPool: VkCommandPool

    fun init(nativeCommandBuffer: vulkan.VkCommandBuffer,
             device: VkDevice,
             commandPool: VkCommandPool) {
        this.native = nativeCommandBuffer
        this.device = device
        this.commandPool = commandPool
    }

    override fun dispose() = memScoped {
        val native = allocArray<VkCommandBufferVar>(1)
        native[0] = this@VkCommandBuffer.native
        vulkan.vkFreeCommandBuffers(device.native, commandPool.native, 1, native)
    }
}

@ExperimentalUnsignedTypes
actual fun vkAllocateCommandBuffers(device: VkDevice, allocateInfo: VkCommandBufferAllocateInfo) = memScoped {
    val natives = allocArray<vulkan.VkCommandBufferVar>(allocateInfo.commandBufferCount)

    checkError(vulkan.vkAllocateCommandBuffers(device.native, allocateInfo.toNative(this), natives))

    List(allocateInfo.commandBufferCount) {
        VkCommandBuffer().apply {
            init(natives[it] ?: throw VkNullError("commandBuffer"),
                    device, allocateInfo.commandPool)
        }
    }
}

@ExperimentalUnsignedTypes
actual fun vkBeginCommandBuffer(commandBuffer: VkCommandBuffer, beginInfo: VkCommandBufferBeginInfo) = memScoped {
    checkError(vulkan.vkBeginCommandBuffer(commandBuffer.native, beginInfo.toNative(this)))
}

@ExperimentalUnsignedTypes
actual fun vkEndCommandBuffer(commandBuffer: VkCommandBuffer) {
    checkError(vulkan.vkEndCommandBuffer(commandBuffer.native))
}

@ExperimentalUnsignedTypes
actual fun vkCmdBeginRenderPass(
        commandBuffer: VkCommandBuffer,
        beginInfo: VkRenderPassBeginInfo,
        contents: VkSubpassContents) = memScoped {
    vulkan.vkCmdBeginRenderPass(commandBuffer.native, beginInfo.toNative(this), contents.value.toUInt())
}

@ExperimentalUnsignedTypes
actual fun vkCmdEndRenderPass(commandBuffer: VkCommandBuffer) {
    vulkan.vkCmdEndRenderPass(commandBuffer.native)
}

@ExperimentalUnsignedTypes
actual fun vkCmdSetViewport(commandBuffer: VkCommandBuffer, firstViewport: Int, viewports: List<VkViewport>) = memScoped {
    vulkan.vkCmdSetViewport(commandBuffer.native, firstViewport.toUInt(), viewports.size.toUInt(), viewports.toNative(this))
}

@ExperimentalUnsignedTypes
actual fun vkCmdSetScissor(commandBuffer: VkCommandBuffer, firstScissor: Int, scissors: List<VkRect2D>) = memScoped {
    vulkan.vkCmdSetScissor(commandBuffer.native, firstScissor.toUInt(), scissors.size.toUInt(), scissors.toNative(this))
}

@ExperimentalUnsignedTypes
actual fun vkCmdBindPipeline(commandBuffer: VkCommandBuffer, pipelineBindPoint: VkPipelineBindPoint, pipeline: VkPipeline) = memScoped {
    vulkan.vkCmdBindPipeline(commandBuffer.native, pipelineBindPoint.value.toUInt(), pipeline.native)
}

@ExperimentalUnsignedTypes
actual fun vkCmdBindVertexBuffers(
        commandBuffer: VkCommandBuffer,
        firstBinding: Int,
        buffers: List<VkBuffer>,
        offsets: List<Long>) = memScoped {
    vulkan.vkCmdBindVertexBuffers(
            commandBuffer.native,
            firstBinding.toUInt(),
            buffers.size.toUInt(),
            buffers.map { it.native }.toNative(this),
            offsets.map { it.toULong() }.toNative(this))
}

@ExperimentalUnsignedTypes
actual fun vkCmdDraw(commandBuffer: VkCommandBuffer, vertexCount: Int, instanceCount: Int, firstVertex: Int, firstInstance: Int) {
    vulkan.vkCmdDraw(commandBuffer.native, vertexCount.toUInt(), instanceCount.toUInt(), firstVertex.toUInt(), firstInstance.toUInt())
}

@ExperimentalUnsignedTypes
actual fun vkCmdPipelineBarrier(
        commandBuffer: VkCommandBuffer,
        srcStageMask: List<VkPipelineStageFlagBits>,
        dstStageMask: List<VkPipelineStageFlagBits>,
        dependencyFlags: List<VkDependencyFlagBits>,
        memoryBarriers: List<VkMemoryBarrier>,
        bufferMemoryBarriers: List<VkBufferMemoryBarrier>,
        imageMemoryBarriers: List<VkImageMemoryBarrier>) = memScoped {

    vulkan.vkCmdPipelineBarrier(
            commandBuffer.native,
            srcStageMask.sumBy { it.value }.toUInt(),
            dstStageMask.sumBy { it.value }.toUInt(),
            dependencyFlags.sumBy { it.value }.toUInt(),
            memoryBarriers.size.toUInt(),
            memoryBarriers.toNative(this),
            bufferMemoryBarriers.size.toUInt(),
            bufferMemoryBarriers.toNative(this),
            imageMemoryBarriers.size.toUInt(),
            imageMemoryBarriers.toNative(this))
}

@ExperimentalUnsignedTypes
actual fun vkCmdClearColorImage(
        commandBuffer: VkCommandBuffer,
        image: VkImage,
        imageLayout: VkImageLayout,
        clearColor: Vector4,
        ranges: List<VkImageSubresourceRange>) = memScoped {
    vulkan.vkCmdClearColorImage(
            commandBuffer.native,
            image.native,
            imageLayout.value.toUInt(),
            clearColor.toNative(this),
            ranges.size.toUInt(),
            ranges.toNative(this))
}

@ExperimentalUnsignedTypes
actual fun vkResetCommandBuffer(commandBuffer: VkCommandBuffer, flags: List<VkCommandBufferResetFlagBits>) {
    vulkan.vkResetCommandBuffer(commandBuffer.native, flags.sumBy { it.value }.toUInt())
}

@ExperimentalUnsignedTypes
actual fun vkCmdCopyBufferToImage(
        commandBuffer: VkCommandBuffer,
        srcBuffer: VkBuffer,
        srcImage: VkImage,
        dstImageLayout: VkImageLayout,
        regions: List<VkBufferImageCopy>) = memScoped {
    vulkan.vkCmdCopyImageToBuffer(
            commandBuffer.native,
            srcImage.native,
            dstImageLayout.value.toUInt(),
            srcBuffer.native,
            regions.size.toUInt(),
            regions.toNative(this))
}

@ExperimentalUnsignedTypes
actual fun vkCmdBindDescriptorSets(
        commandBuffer: VkCommandBuffer,
        pipelineBindPoint: VkPipelineBindPoint,
        layout: VkPipelineLayout,
        firstSet: Int,
        descriptorSets: List<VkDescriptorSet>,
        dynamicOffsets: List<Int>) = memScoped {
    vulkan.vkCmdBindDescriptorSets(
            commandBuffer.native,
            pipelineBindPoint.value.toUInt(),
            layout.native,
            firstSet.toUInt(),
            descriptorSets.size.toUInt(),
            descriptorSets.map { it.native }.toNative(this),
            dynamicOffsets.size.toUInt(),
            dynamicOffsets.map { it.toUInt() }.toNative(this))
}
