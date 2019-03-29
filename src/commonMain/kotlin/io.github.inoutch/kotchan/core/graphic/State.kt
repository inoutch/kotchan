package io.github.inoutch.kotchan.core.graphic

enum class CullMode {
    None,
    Front,
    Back,
}

enum class PolygonMode {
    Fill,
    Line,
    Point,
}

enum class BlendFactor {
    One,
    Zero,
    SrcColor,
    OneMinusSrcColor,
    DstColor,
    OneMinusDstColor,
    SrcAlpha,
    OneMinusSrcAlpha,
    DstAlpha,
    OneMinusDstAlpha,
}
