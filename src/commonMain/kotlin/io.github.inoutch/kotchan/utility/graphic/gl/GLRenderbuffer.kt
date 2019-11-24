package io.github.inoutch.kotchan.utility.graphic.gl

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.utility.Disposable

data class GLRenderbuffer(val id: Int) : Disposable {

    override fun dispose() {
        core.gl?.deleteRenderBuffer(id)
    }
}
