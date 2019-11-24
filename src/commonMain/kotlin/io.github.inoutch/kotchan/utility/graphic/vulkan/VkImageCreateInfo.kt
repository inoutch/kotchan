package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkImageCreateInfo(
    val flags: Int,
    val imageType: VkImageType,
    val extent: VkExtent3D,
    val format: VkFormat,
    val mipLevels: Int,
    val arrayLayers: Int,
    val samples: VkSampleCountFlagBits,
    val tiling: VkImageTiling,
    val usage: List<VkImageUsageFlagBits>,
    val sharingMode: VkSharingMode,
    val queueFamilyIndices: List<Int>,
    val initialLayout: VkImageLayout
)
