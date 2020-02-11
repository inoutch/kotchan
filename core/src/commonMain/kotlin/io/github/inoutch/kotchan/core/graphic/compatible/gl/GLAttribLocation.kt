package io.github.inoutch.kotchan.core.graphic.compatible.gl

enum class GLAttribLocation(val value: Int, val locationName: String) {
    ATTRIBUTE_POSITION(0, "point"),
    ATTRIBUTE_TEXCOORD(1, "texcoord"),
    ATTRIBUTE_COLOR(2, "color"),
}
