package interop.graphic

import kotchan.Engine

data class GLVBO(val id: Int) {
    var isDestroyed = false

    fun destroy() {
        if (isDestroyed) {
            throw Error("already destroyed")
        }
        isDestroyed = true
        Engine.getInstance().gl.destroyVBO(id)
    }

    protected fun finalize() {
        if (!isDestroyed) {
            throw Error("must call destroy function")
        }
    }
}