package io.github.inoutch.kotchan.core.graphic.shader

import io.github.inoutch.kotchan.utility.graphic.gl.GLShader
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkShaderModule

data class Shader(val vkShader: VKBundle?, val glShader: GLShader?) {
    class VKBundle(val vert: VkShaderModule, val frag: VkShaderModule)
}
