package io.github.inoutch.kotchan.core.graphic.shader

import io.github.inoutch.kotchan.utility.graphic.gl.GLShaderProgram
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkShaderModule

data class Shader(val vkShader: VkShaderModule, val glShader: GLShaderProgram?)
