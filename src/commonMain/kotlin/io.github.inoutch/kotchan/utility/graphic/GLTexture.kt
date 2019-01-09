package io.github.inoutch.kotchan.utility.graphic

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.destruction.StrictDestruction

class GLTexture(val id: Int, val width: Int, val height: Int) : StrictDestruction() {

    private val gl = KotchanCore.instance.gl

    var filterType: GLFilterType = GLFilterType.Nearest

    companion object {
        val empty = GLTexture(0, 0, 0)
    }

    fun use() {
        check()
        gl.useTexture(this)
        gl.filterTexture(filterType)
    }

    override fun destroy() {
        if (id == 0) {
            // pass through empty texture
            return
        }

        super.destroy()
        gl.deleteTexture(id)
    }
}