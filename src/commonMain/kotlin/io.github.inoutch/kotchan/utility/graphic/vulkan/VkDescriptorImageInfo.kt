package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkDescriptorImageInfo(
    val sampler: VkSampler,
    val imageView: VkImageView,
    val imageLayout: VkImageLayout
)
