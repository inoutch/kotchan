package io.github.inoutch.kotchan.core.graphic.compatible.shader

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.DescriptorSet

abstract class ShaderProgram(
        shaderSource: ShaderSource,
        descriptorSets: List<DescriptorSet>
) : Disposer() {
    val shader = graphic.createShader(shaderSource)

    val descriptorSets = listOf(*descriptorSets.toTypedArray())
//    private val viewProjectionMatrixUniform = Uniform

    init {
        add(shader)
    }

    fun prepare() {

    }
}
