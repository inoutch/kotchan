package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkRenderPassCreateInfo(
    val flags: Int,
    val attachments: List<VkAttachmentDescription>,
    val subpasses: List<VkSubpassDescription>,
    val dependencies: List<VkSubpassDependency>,
    val correlatedViewMasks: List<Int>
)
