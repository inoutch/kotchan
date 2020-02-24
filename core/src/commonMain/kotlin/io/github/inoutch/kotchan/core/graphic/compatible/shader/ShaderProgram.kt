package io.github.inoutch.kotchan.core.graphic.compatible.shader

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.compatible.shader.attribute.Attribute
import io.github.inoutch.kotchan.core.graphic.compatible.shader.attribute.AttributeType
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.DescriptorSet

abstract class ShaderProgram(
    shaderSource: ShaderSource,
    initialDescriptorSets: List<DescriptorSet>,
    attributes: List<Attribute> = listOf(
            Attribute(0, "point", 3, AttributeType.FLOAT),
            Attribute(1, "color", 4, AttributeType.FLOAT),
            Attribute(2, "texcoord", 2, AttributeType.FLOAT)
    )
) : Disposer() {
    val shader = graphic.createShader(shaderSource, attributes)

    val descriptorSets: List<DescriptorSet> = initialDescriptorSets

    init {
        add(shader)
        initialDescriptorSets.forEach { add(it) }
    }
}
