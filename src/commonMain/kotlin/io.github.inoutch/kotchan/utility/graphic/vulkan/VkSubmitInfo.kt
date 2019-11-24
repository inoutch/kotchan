package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkSubmitInfo(
    val waitSemaphores: List<VkSemaphore>,
    val waitDstStageMask: List<VkPipelineStageFlagBits>,
    val commandBuffers: List<VkCommandBuffer>,
    val signalSemaphores: List<VkSemaphore>
)
