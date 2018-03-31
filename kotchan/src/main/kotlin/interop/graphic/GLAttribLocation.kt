package interop.graphic

enum class GLAttribLocation(val value: Int, val locationName: String) {
    ATTRIBUTE_POSITION(1, "position"),
    ATTRIBUTE_TEXCOORD(2, "texcoord"),
    ATTRIBUTE_COLOR(3, "color"),
}