package io.github.inoutch.kotchan.core.graphic.compatible.gl

enum class GLAttribLocation(val value: Int, val locationName: String) {
    ATTRIBUTE_POSITION(1, "point"),
    ATTRIBUTE_TEXCOORD(2, "texcoord"),
    ATTRIBUTE_COLOR(3, "color"),
}
