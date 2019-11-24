package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkRenderPassBeginInfo(
    val renderPass: VkRenderPass,
    val framebuffer: VkFramebuffer,
    val renderArea: VkRect2D,
    val clearValues: List<VkClearValue>
)
