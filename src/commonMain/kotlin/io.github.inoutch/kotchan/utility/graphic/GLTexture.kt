package io.github.inoutch.kotchan.utility.graphic

import io.github.inoutch.kotchan.core.KotchanCore

class GLTexture(val id: Int, val width: Int, val height: Int) {

    private val gl = KotchanCore.instance.gl

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
        if (id == 0) {
            return
        }
        if (isDestroyed) {
            throw Error("already destroyed")
        }
        isDestroyed = true
        gl.destroyTexture(id)
    }
}