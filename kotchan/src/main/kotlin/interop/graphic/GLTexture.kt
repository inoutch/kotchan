package interop.graphic

import kotchan.Engine

class GLTexture(val id: Int, val width: Int, val height: Int) {
    var filterType: GLFilterType = GLFilterType.Nearest

    companion object {
        val empty = GLTexture(0, 0, 0)
    }

    fun use() {
        Engine.getInstance().gl.useTexture(this)
        Engine.getInstance().gl.filterTexture(filterType)
    }
}