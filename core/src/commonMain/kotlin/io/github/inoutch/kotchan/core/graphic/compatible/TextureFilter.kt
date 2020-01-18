package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotlin.gl.api.GL_LINEAR
import io.github.inoutch.kotlin.gl.api.GL_NEAREST
import io.github.inoutch.kotlin.gl.api.GLint
import io.github.inoutch.kotlin.vulkan.api.VkFilter

enum class TextureFilter(val glParam: GLint, val vkParam: VkFilter) {
    NEAREST(GL_NEAREST, VkFilter.VK_FILTER_NEAREST),
    LINEAR(GL_LINEAR, VkFilter.VK_FILTER_LINEAR),
}
