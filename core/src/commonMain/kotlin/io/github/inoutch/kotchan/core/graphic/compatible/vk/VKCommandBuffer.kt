package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkCommandBuffer
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferBeginInfo
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferResetFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkResult
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
}
