package io.github.inoutch.kotchan.utility.graphic

import io.github.inoutch.kotchan.core.KotchanCore

data class GLVBO(val id: Int) {
    var isDestroyed = false

    fun destroy() {
        if (isDestroyed) {
            throw Error("already destroyed")
        }
        isDestroyed = true
        KotchanCore.instance.gl.destroyVBO(id)
    }
}