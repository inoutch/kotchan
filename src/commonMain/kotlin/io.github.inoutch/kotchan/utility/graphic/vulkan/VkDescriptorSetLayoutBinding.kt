package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkDescriptorSetLayoutBinding(
    val binding: Int,
    val descriptorType: VkDescriptorType,
    val descriptorCount: Int,
    val stageFlags: List<VkShaderStageFlagBits>,
    val immutableSamplers: VkSampler?
)
