package io.github.inoutch.kotchan.core.graphic.shader

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.shader.unform.UniformMatrix4fv
import io.github.inoutch.kotchan.core.shader.simpleFragCode
import io.github.inoutch.kotchan.core.shader.simpleFragText
import io.github.inoutch.kotchan.core.shader.simpleVertCode
import io.github.inoutch.kotchan.core.shader.simpleVertText
import io.github.inoutch.kotchan.utility.type.Matrix4

class SimpleShaderProgram : ShaderProgram(createShader()) {

    companion object {
        fun createShader(): Shader {
            val vert = ShaderProgram.ShaderSource(simpleVertText, simpleVertCode)
            val frag = ShaderProgram.ShaderSource(simpleFragText, simpleFragCode)
            return KotchanCore.instance.graphicsApi.createShader(vert, frag)
        }
    }
}
