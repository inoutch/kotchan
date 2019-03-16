package io.github.inoutch.kotchan.core.graphic.shader

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.shader.unform.UniformMatrix4fv
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.type.Matrix4

abstract class ShaderProgram(
        val shader: Shader,
        initDescriptors: List<Descriptor> = listOf()) : Disposable {

    class ShaderSource(
            val text: String,
            val binary: ByteArray)

    private val viewProjectionMatrixUniform = UniformMatrix4fv(0, "u_viewProjectionMatrix")

    private val primarySampler = Sampler(1, "u_texture0")

    val descriptors = listOf(*initDescriptors.toTypedArray(),
            viewProjectionMatrixUniform,
            primarySampler)

    open fun prepare(delta: Float, mvpMatrix: Matrix4, texture: Texture?) {
        viewProjectionMatrixUniform.set(mvpMatrix)
        primarySampler.set(texture ?: instance.graphicsApi.emptyTexture())
    }

    override fun dispose() {
        descriptors.forEach { it.dispose() }
    }
}
