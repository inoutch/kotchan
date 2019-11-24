package io.github.inoutch.kotchan.utility.graphic.gl

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.utility.Disposable

class GLTexture(val id: Int, val width: Int, val height: Int) : Disposable {

    private val gl = core.gl as GL

    var filterType: GLFilterType = GLFilterType.Nearest

    companion object {
        val empty = GLTexture(0, 0, 0)
    }

    fun use() {
        gl.useTexture(this)
        gl.filterTexture(filterType)
    }

    override fun dispose() {
        if (id == 0) {
            // pass through empty texture
            return
        }
        gl.deleteTexture(id)
    }
}
