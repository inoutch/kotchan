package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotlin.vulkan.api.VkSamplerMipmapMode

enum class TextureMipmapMode(val vkParam: VkSamplerMipmapMode) {
    NEAREST(VkSamplerMipmapMode.VK_SAMPLER_MIPMAP_MODE_NEAREST),
    LINER(VkSamplerMipmapMode.VK_SAMPLER_MIPMAP_MODE_LINEAR),
}
