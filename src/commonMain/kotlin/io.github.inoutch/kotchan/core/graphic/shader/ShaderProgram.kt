package io.github.inoutch.kotchan.core.graphic.shader

import io.github.inoutch.kotchan.core.graphic.shader.unform.UniformMatrix4fv
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.type.Matrix4

abstract class ShaderProgram(val shader: Shader, initDescriptorSets: List<DescriptorSet> = listOf()) : Disposable {

    class ShaderSource(
            val text: String,
            val binary: ByteArray)

    private val viewProjectionMatrixUniform = UniformMatrix4fv(0, "u_viewProjectionMatrix")

    private val sampler = Sampler(1, "u_texture0")

    val descriptorSets = listOf(*initDescriptorSets.toTypedArray(), viewProjectionMatrixUniform, sampler)

    open fun prepare(delta: Float, mvpMatrix: Matrix4, textures: List<Texture>) {
        viewProjectionMatrixUniform.set(mvpMatrix)
        sampler.set(textures.firstOrNull() ?: Texture.emptyTexture())
    }

    override fun dispose() {
        shader.glShader?.dispose()
        shader.vkShader?.dispose()
        descriptorSets.forEach { it.dispose() }
    }
}
