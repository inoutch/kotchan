package io.github.inoutch.kotchan.core.graphic.shader

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.shader.unform.UniformMatrix4fv
import io.github.inoutch.kotchan.core.shader.simpleFragCode
import io.github.inoutch.kotchan.core.shader.simpleFragText
import io.github.inoutch.kotchan.core.shader.simpleVertCode
import io.github.inoutch.kotchan.core.shader.simpleVertText
import io.github.inoutch.kotchan.utility.type.Matrix4

class SimpleShaderProgram(
        private val uniformMatrix4fv: UniformMatrix4fv = UniformMatrix4fv(0, "u_viewProjectionMatrix"),
        val sampler: Sampler = Sampler(1, "u_texture0")) :
        ShaderProgram(createShader(), listOf(uniformMatrix4fv, sampler)) {

    companion object {
        fun createShader(): Shader {
            val vert = ShaderProgram.ShaderSource(simpleVertText, simpleVertCode)
            val frag = ShaderProgram.ShaderSource(simpleFragText, simpleFragCode)
            return KotchanCore.instance.graphicsApi.createShader(vert, frag)
        }
    }

    fun update(delta: Float, camera: Camera) {
        uniformMatrix4fv.set(camera.combine)
    }
}
