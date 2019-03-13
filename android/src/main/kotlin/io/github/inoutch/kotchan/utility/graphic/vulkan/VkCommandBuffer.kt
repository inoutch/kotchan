package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.type.Vector4

actual class VkCommandBuffer : Disposable {
    override fun dispose() {}
}

actual fun vkAllocateCommandBuffers(device: VkDevice, allocateInfo: VkCommandBufferAllocateInfo): List<VkCommandBuffer> {
    // not implementation
    return listOf()
}

actual fun vkBeginCommandBuffer(commandBuffer: VkCommandBuffer, beginInfo: VkCommandBufferBeginInfo) {
    // not implementation
}

actual fun vkEndCommandBuffer(commandBuffer: VkCommandBuffer) {
    // not implementation
}

actual fun vkCmdBeginRenderPass(commandBuffer: VkCommandBuffer, beginInfo: VkRenderPassBeginInfo, contents: VkSubpassContents) {
    // not implementation
}

actual fun vkCmdEndRenderPass(commandBuffer: VkCommandBuffer) {
    // not implementation
}

actual fun vkCmdSetViewport(commandBuffer: VkCommandBuffer, firstViewport: Int, viewports: List<VkViewport>) {
    // not implementation
}

actual fun vkCmdSetScissor(commandBuffer: VkCommandBuffer, firstScissor: Int, scissors: List<VkRect2D>) {
    // not implementation
}

actual fun vkCmdBindPipeline(commandBuffer: VkCommandBuffer, pipelineBindPoint: VkPipelineBindPoint, pipeline: VkPipeline) {
    // not implementation
}

actual fun vkCmdBindVertexBuffers(commandBuffer: VkCommandBuffer, firstBinding: Int, buffers: List<VkBuffer>, offsets: List<Long>) {
    // not implementation
}

actual fun vkCmdDraw(commandBuffer: VkCommandBuffer, vertexCount: Int, instanceCount: Int, firstVertex: Int, firstInstance: Int) {
    // not implementation
}

actual fun vkCmdPipelineBarrier(
        commandBuffer: VkCommandBuffer,
        srcStageMask: List<VkPipelineStageFlagBits>,
        dstStageMask: List<VkPipelineStageFlagBits>,
        dependencyFlags: List<VkDependencyFlagBits>,
        memoryBarriers: List<VkMemoryBarrier>,
        bufferMemoryBarriers: List<VkBufferMemoryBarrier>,
        imageMemoryBarriers: List<VkImageMemoryBarrier>) {
    // not implementation
}

actual fun vkCmdClearColorImage(
        commandBuffer: VkCommandBuffer,
        image: VkImage,
        imageLayout: VkImageLayout,
        clearColor: Vector4,
        ranges: List<VkImageSubresourceRange>) {
    // not implementation
}

actual fun vkResetCommandBuffer(commandBuffer: VkCommandBuffer, flags: List<VkCommandBufferResetFlagBits>) {
    // not implementation
}

actual fun vkCmdCopyBufferToImage(commandBuffer: VkCommandBuffer, srcBuffer: VkBuffer, srcImage: VkImage, dstImageLayout: VkImageLayout, regions: List<VkBufferImageCopy>) {
    // not implementation
}

actual fun vkCmdBindDescriptorSets(commandBuffer: VkCommandBuffer, pipelineBindPoint: VkPipelineBindPoint, layout: VkDescriptorSetLayout, firstSet: Int, descriptorSets: List<VkDescriptorSet>, dynamicOffsets: List<Int>) {
}
