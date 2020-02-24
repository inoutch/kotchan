package io.github.inoutch.kotchan.core.graphic.compatible.gl

enum class GLAttribLocation(val value: Int, val locationName: String) {
    ATTRIBUTE_POSITION(0, "point"),
    ATTRIBUTE_COLOR(1, "color"),
    ATTRIBUTE_TEXCOORD(2, "texcoord"),
}
