package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotlin.gl.api.GL_CLAMP_TO_EDGE
import io.github.inoutch.kotlin.gl.api.GL_MIRRORED_REPEAT
import io.github.inoutch.kotlin.gl.api.GL_REPEAT
import io.github.inoutch.kotlin.gl.api.GLint
import io.github.inoutch.kotlin.vulkan.api.VkSamplerAddressMode

enum class TextureAddressMode(val glParam: GLint, val vkParam: VkSamplerAddressMode) {
    REPEAT(GL_REPEAT, VkSamplerAddressMode.VK_SAMPLER_ADDRESS_MODE_REPEAT),
    MIRRORED_REPEAT(GL_MIRRORED_REPEAT, VkSamplerAddressMode.VK_SAMPLER_ADDRESS_MODE_MIRRORED_REPEAT),
    CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE, VkSamplerAddressMode.VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE),
//    CLAMP_TO_BORDER(,VkSamplerAddressMode.VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_BORDER),
//    MIRRORED_CLAMP_TO_EDGE(,VkSamplerAddressMode.VK_SAMPLER_ADDRESS_MODE_MIRROR_CLAMP_TO_EDGE),
}
