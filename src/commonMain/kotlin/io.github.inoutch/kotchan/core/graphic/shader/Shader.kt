package io.github.inoutch.kotchan.core.graphic.shader

import io.github.inoutch.kotchan.utility.graphic.gl.GLShader
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKShader

data class Shader(val vkShader: VKShader?, val glShader: GLShader?)
