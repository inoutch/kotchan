package io.github.inoutch.kotchan.core.graphic.compatible.shader

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.DescriptorSet

abstract class ShaderProgram(
        shaderSource: ShaderSource,
        initialDescriptorSets: List<DescriptorSet>
) : Disposer() {
    val shader = graphic.createShader(shaderSource)

    val descriptorSets: List<DescriptorSet> = initialDescriptorSets

    init {
        add(shader)
        initialDescriptorSets.forEach { add(it) }
    }
}
