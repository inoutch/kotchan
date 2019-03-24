package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.gl.GLVBO
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKBufferMemory

data class VertexBuffer(
        val vkBuffer: VKBufferMemory?,
        val glBuffer: GLVBO?) : Disposable {

    override fun dispose() {
        vkBuffer?.dispose()
        glBuffer?.dispose()
    }
}
