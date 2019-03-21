package io.github.inoutch.kotchan.utility.graphic.gl

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.utility.Disposable

data class GLFramebuffer(val id: Int) : Disposable {
    override fun dispose() {
        KotchanCore.instance.gl?.deleteFrameBuffer(id)
    }
}
