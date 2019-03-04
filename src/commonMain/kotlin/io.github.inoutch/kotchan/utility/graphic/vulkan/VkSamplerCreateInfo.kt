package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkSamplerCreateInfo(
        val flags: Int,
        val magFilter: VkFilter,
        val minFilter: VkFilter,
        val mipmapMode: VkSamplerMipmapMode,
        val addressModeU: VkSamplerAddressMode,
        val addressModeV: VkSamplerAddressMode,
        val addressModeW: VkSamplerAddressMode,
        val mipLodBias: Float,
        val anisotropyEnable: Boolean,
        val maxAnisotropy: Float,
        val compareEnable: Boolean,
        val compareOp: VkCompareOp,
        val minLod: Float,
        val maxLod: Float,
        val borderColor: VkBorderColor,
        val unnormalizedCoordinates: Boolean)
