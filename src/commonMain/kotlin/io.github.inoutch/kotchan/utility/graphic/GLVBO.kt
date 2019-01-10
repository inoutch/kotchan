package io.github.inoutch.kotchan.utility.graphic

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.destruction.StrictDestruction

data class GLVBO(val id: Int) : StrictDestruction() {

    override fun destroy() {
        super.destroy()
        KotchanCore.instance.gl.deleteVBO(id)
    }
}