package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkDescriptorSetLayoutCreateInfo(
    val flags: Int,
    val bindings: List<VkDescriptorSetLayoutBinding>
)
