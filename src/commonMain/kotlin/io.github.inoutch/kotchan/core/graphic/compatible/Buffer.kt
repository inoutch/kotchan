package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.gl.GLVBO
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkBuffer
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkDeviceMemory

data class Buffer(
        val vkBuffer: VKBundle?,
        val glBuffer: GLBundle?) : Disposable {

    data class VKBundle(val buffer: VkBuffer, val memory: VkDeviceMemory, val allocationSize: Long)

    data class GLBundle(val vbo: GLVBO)

    override fun dispose() {
        vkBuffer?.apply {
            buffer.dispose()
            memory.dispose()
        }
        glBuffer?.apply {
            vbo.dispose()
        }
    }
}
