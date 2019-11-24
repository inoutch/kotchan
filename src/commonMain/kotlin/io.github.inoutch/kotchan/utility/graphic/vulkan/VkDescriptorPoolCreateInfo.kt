package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkDescriptorPoolCreateInfo(
    val flags: List<VkDescriptorPoolCreateFlagBits>,
    val maxSets: Int,
    val poolSizes: List<VkDescriptorPoolSize>
)
