package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.type.Vector4

expect class VkCommandBuffer : Disposable

expect fun vkAllocateCommandBuffers(
    device: VkDevice,
    allocateInfo: VkCommandBufferAllocateInfo
): List<VkCommandBuffer>

expect fun vkBeginCommandBuffer(
    commandBuffer: VkCommandBuffer,
    beginInfo: VkCommandBufferBeginInfo
)

expect fun vkEndCommandBuffer(
    commandBuffer: VkCommandBuffer
)

expect fun vkCmdBeginRenderPass(
    commandBuffer: VkCommandBuffer,
    beginInfo: VkRenderPassBeginInfo,
    contents: VkSubpassContents
)

expect fun vkCmdEndRenderPass(
    commandBuffer: VkCommandBuffer
)

expect fun vkCmdSetViewport(
    commandBuffer: VkCommandBuffer,
    firstViewport: Int,
    viewports: List<VkViewport>
)

expect fun vkCmdSetScissor(
    commandBuffer: VkCommandBuffer,
    firstScissor: Int,
    scissors: List<VkRect2D>
)

expect fun vkCmdBindPipeline(
    commandBuffer: VkCommandBuffer,
    pipelineBindPoint: VkPipelineBindPoint,
    pipeline: VkPipeline
)

expect fun vkCmdBindVertexBuffers(
    commandBuffer: VkCommandBuffer,
    firstBinding: Int,
    buffers: List<VkBuffer>,
    offsets: List<Long>
)

expect fun vkCmdDraw(
    commandBuffer: VkCommandBuffer,
    vertexCount: Int,
    instanceCount: Int,
    firstVertex: Int,
    firstInstance: Int
)

expect fun vkCmdPipelineBarrier(
    commandBuffer: VkCommandBuffer,
    srcStageMask: List<VkPipelineStageFlagBits>,
    dstStageMask: List<VkPipelineStageFlagBits>,
    dependencyFlags: List<VkDependencyFlagBits>,
    memoryBarriers: List<VkMemoryBarrier>,
    bufferMemoryBarriers: List<VkBufferMemoryBarrier>,
    imageMemoryBarriers: List<VkImageMemoryBarrier>
)

expect fun vkCmdClearColorImage(
    commandBuffer: VkCommandBuffer,
    image: VkImage,
    imageLayout: VkImageLayout,
    clearColor: Vector4,
    ranges: List<VkImageSubresourceRange>
)

expect fun vkCmdClearDepthStencilImage(
    commandBuffer: VkCommandBuffer,
    image: VkImage,
    imageLayout: VkImageLayout,
    depthStencilValue: VkClearDepthStencilValue,
    ranges: List<VkImageSubresourceRange>
)

expect fun vkResetCommandBuffer(
    commandBuffer: VkCommandBuffer,
    flags: List<VkCommandBufferResetFlagBits>
)

expect fun vkCmdCopyBufferToImage(
    commandBuffer: VkCommandBuffer,
    srcBuffer: VkBuffer,
    srcImage: VkImage,
    dstImageLayout: VkImageLayout,
    regions: List<VkBufferImageCopy>
)

expect fun vkCmdBindDescriptorSets(
    commandBuffer: VkCommandBuffer,
    pipelineBindPoint: VkPipelineBindPoint,
    layout: VkPipelineLayout,
    firstSet: Int,
    descriptorSets: List<VkDescriptorSet>,
    dynamicOffsets: List<Int>
)
