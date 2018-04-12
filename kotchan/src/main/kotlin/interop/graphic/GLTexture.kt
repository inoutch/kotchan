package interop.graphic

import kotchan.Engine

class GLTexture(val id: Int, val width: Int, val height: Int) {
    private val gl = Engine.getInstance().gl
    var isDestroyed = false
        private set
    var filterType: GLFilterType = GLFilterType.Nearest

    companion object {
        val empty = GLTexture(0, 0, 0)
    }

    fun use() {
        if (isDestroyed) {
            throw Error("already destroyed")
        }
        gl.useTexture(this)
        gl.filterTexture(filterType)
    }

    fun destroy() {
        if (isDestroyed) {
            throw Error("already destroyed")
        }
        isDestroyed = true
        gl.destroyTexture(id)
    }

    protected fun finalize() {
        if (!isDestroyed) {
            throw Error("must call destroy function")
        }
    }
}