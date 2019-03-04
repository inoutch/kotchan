package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkCommandBuffer : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkAllocateCommandBuffers(device: VkDevice, allocateInfo: VkCommandBufferAllocateInfo): List<VkCommandBuffer> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

actual fun vkBeginCommandBuffer(commandBuffer: VkCommandBuffer, beginInfo: VkCommandBufferBeginInfo) {
}

actual fun vkEndCommandBuffer(commandBuffer: VkCommandBuffer) {
}

actual fun vkCmdBeginRenderPass(commandBuffer: VkCommandBuffer, beginInfo: VkRenderPassBeginInfo, contents: VkSubpassContents) {
}

actual fun vkCmdEndRenderPass(commandBuffer: VkCommandBuffer) {
}

actual fun vkCmdSetViewport(commandBuffer: VkCommandBuffer, firstViewport: Int, viewports: List<VkViewport>) {
}

actual fun vkCmdSetScissor(commandBuffer: VkCommandBuffer, firstScissor: Int, scissors: List<VkRect2D>) {
}

actual fun vkCmdBindPipeline(commandBuffer: VkCommandBuffer, pipelineBindPoint: VkPipelineBindPoint, pipeline: VkPipeline) {
}

actual fun vkCmdBindVertexBuffers(commandBuffer: VkCommandBuffer, firstBinding: Int, buffers: List<VkBuffer>, offsets: List<Long>) {
}

actual fun vkCmdDraw(commandBuffer: VkCommandBuffer, vertexCount: Int, instanceCount: Int, firstVertex: Int, firstInstance: Int) {
}

actual fun vkCmdPipelineBarrier(commandBuffer: VkCommandBuffer, srcStageMask: List<VkPipelineStageFlagBits>, dstStageMask: List<VkPipelineStageFlagBits>, dependencyFlags: List<VkDependencyFlagBits>, memoryBarriers: List<VkMemoryBarrier>, bufferMemoryBarriers: List<VkBufferMemoryBarrier>, imageMemoryBarriers: List<VkImageMemoryBarrier>) {
}
