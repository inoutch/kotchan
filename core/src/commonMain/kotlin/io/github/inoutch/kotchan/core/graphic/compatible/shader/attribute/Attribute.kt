package io.github.inoutch.kotchan.core.graphic.compatible.shader.attribute

data class Attribute(
        val location: Int,
        val locationName: String,
        val stride: Int,
        val type: AttributeType
)
