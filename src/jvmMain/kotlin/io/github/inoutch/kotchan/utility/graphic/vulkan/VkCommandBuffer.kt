package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import io.github.inoutch.kotchan.utility.type.Vector4
import org.lwjgl.vulkan.VK10
import org.lwjgl.vulkan.VK10.vkFreeCommandBuffers

actual class VkCommandBuffer : Disposable {
    lateinit var native: org.lwjgl.vulkan.VkCommandBuffer
        private set
    lateinit var device: VkDevice
        private set
    lateinit var commandPool: VkCommandPool
        private set

    fun init(nativeCommandBuffer: org.lwjgl.vulkan.VkCommandBuffer, device: VkDevice, commandPool: VkCommandPool) {
        this.native = nativeCommandBuffer
        this.device = device
        this.commandPool = commandPool
    }

    override fun dispose() {
        vkFreeCommandBuffers(device.native, commandPool.native, native)
    }
}

actual fun vkAllocateCommandBuffers(
    device: VkDevice,
    allocateInfo: VkCommandBufferAllocateInfo
) = memScoped {

    val native = allocPointer(allocateInfo.commandBufferCount)
    checkError(VK10.vkAllocateCommandBuffers(
            device.native,
            allocateInfo.toNative(this),
            native))

    List(allocateInfo.commandBufferCount) { native.get(it) }.map {
        VkCommandBuffer().apply {
            init(org.lwjgl.vulkan.VkCommandBuffer(it, device.native), device, allocateInfo.commandPool)
        }
    }
}

actual fun vkBeginCommandBuffer(
    commandBuffer: VkCommandBuffer,
    beginInfo: VkCommandBufferBeginInfo
) = memScoped {
    checkError(VK10.vkBeginCommandBuffer(commandBuffer.native, beginInfo.toNative(this)))
}

actual fun vkEndCommandBuffer(
    commandBuffer: VkCommandBuffer
) {
    checkError(VK10.vkEndCommandBuffer(commandBuffer.native))
}

actual fun vkCmdBeginRenderPass(
    commandBuffer: VkCommandBuffer,
    beginInfo: VkRenderPassBeginInfo,
    contents: VkSubpassContents
) = memScoped {
    VK10.vkCmdBeginRenderPass(commandBuffer.native, beginInfo.toNative(this), contents.value)
}

actual fun vkCmdEndRenderPass(
    commandBuffer: VkCommandBuffer
) {
    VK10.vkCmdEndRenderPass(commandBuffer.native)
}

actual fun vkCmdSetViewport(
    commandBuffer: VkCommandBuffer,
    firstViewport: Int,
    viewports: List<VkViewport>
) = memScoped {
    VK10.vkCmdSetViewport(commandBuffer.native, firstViewport, viewports.toNative(this)
            ?: throw VkNullError("viewports"))
}

actual fun vkCmdSetScissor(
    commandBuffer: VkCommandBuffer,
    firstScissor: Int,
    scissors: List<VkRect2D>
) = memScoped {
    VK10.vkCmdSetScissor(commandBuffer.native, firstScissor, scissors.toNative(this)
            ?: throw VkNullError("scissors"))
}

actual fun vkCmdBindPipeline(
    commandBuffer: VkCommandBuffer,
    pipelineBindPoint: VkPipelineBindPoint,
    pipeline: VkPipeline
) {
    VK10.vkCmdBindPipeline(commandBuffer.native, pipelineBindPoint.value, pipeline.native)
}

actual fun vkCmdBindVertexBuffers(
    commandBuffer: VkCommandBuffer,
    firstBinding: Int,
    buffers: List<VkBuffer>,
    offsets: List<Long>
) = memScoped {
    VK10.vkCmdBindVertexBuffers(
            commandBuffer.native, firstBinding, buffers.map { it.native }.toLongArray(),
            offsets.toLongArray())
}

actual fun vkCmdDraw(
    commandBuffer: VkCommandBuffer,
    vertexCount: Int,
    instanceCount: Int,
    firstVertex: Int,
    firstInstance: Int
) {
    VK10.vkCmdDraw(commandBuffer.native, vertexCount, instanceCount, firstVertex, firstInstance)
}

actual fun vkCmdPipelineBarrier(
    commandBuffer: VkCommandBuffer,
    srcStageMask: List<VkPipelineStageFlagBits>,
    dstStageMask: List<VkPipelineStageFlagBits>,
    dependencyFlags: List<VkDependencyFlagBits>,
    memoryBarriers: List<VkMemoryBarrier>,
    bufferMemoryBarriers: List<VkBufferMemoryBarrier>,
    imageMemoryBarriers: List<VkImageMemoryBarrier>
) = memScoped {
    VK10.vkCmdPipelineBarrier(
            commandBuffer.native,
            srcStageMask.sumBy { it.value },
            dstStageMask.sumBy { it.value },
            dependencyFlags.sumBy { it.value },
            memoryBarriers.toNative(this),
            bufferMemoryBarriers.toNative(this),
            imageMemoryBarriers.toNative(this))
}

actual fun vkCmdClearColorImage(
    commandBuffer: VkCommandBuffer,
    image: VkImage,
    imageLayout: VkImageLayout,
    clearColor: Vector4,
    ranges: List<VkImageSubresourceRange>
) = memScoped {
    VK10.vkCmdClearColorImage(
            commandBuffer.native,
            image.native,
            imageLayout.value,
            clearColor.toNative(this),
            ranges.toNative(this))
}

actual fun vkCmdClearDepthStencilImage(
    commandBuffer: VkCommandBuffer,
    image: VkImage,
    imageLayout: VkImageLayout,
    depthStencilValue: VkClearDepthStencilValue,
    ranges: List<VkImageSubresourceRange>
) = memScoped {
    VK10.vkCmdClearDepthStencilImage(
            commandBuffer.native,
            image.native,
            imageLayout.value,
            depthStencilValue.toNative(this),
            ranges.toNative(this))
}

actual fun vkResetCommandBuffer(commandBuffer: VkCommandBuffer, flags: List<VkCommandBufferResetFlagBits>) {
    VK10.vkResetCommandBuffer(commandBuffer.native, flags.sumBy { it.value })
}

actual fun vkCmdCopyBufferToImage(
    commandBuffer: VkCommandBuffer,
    srcBuffer: VkBuffer,
    srcImage: VkImage,
    dstImageLayout: VkImageLayout,
    regions: List<VkBufferImageCopy>
) = memScoped {
    VK10.vkCmdCopyBufferToImage(
            commandBuffer.native,
            srcBuffer.native,
            srcImage.native,
            dstImageLayout.value,
            regions.toNative(this))
}

actual fun vkCmdBindDescriptorSets(
    commandBuffer: VkCommandBuffer,
    pipelineBindPoint: VkPipelineBindPoint,
    layout: VkPipelineLayout,
    firstSet: Int,
    descriptorSets: List<VkDescriptorSet>,
    dynamicOffsets: List<Int>
) = memScoped {
    VK10.vkCmdBindDescriptorSets(
            commandBuffer.native,
            pipelineBindPoint.value,
            layout.native,
            firstSet,
            descriptorSets.map { it.native }.toLongArray().toNative(this),
            dynamicOffsets.toIntArray().toNative(this))
}
