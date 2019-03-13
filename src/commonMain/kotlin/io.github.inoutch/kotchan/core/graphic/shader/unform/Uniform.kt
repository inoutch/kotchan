package io.github.inoutch.kotchan.core.graphic.shader.unform

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.VKUniformBuffer

abstract class Uniform(val binding: Int, val uniformName: String) : Disposable {
    var vkUniform: VKUniformBuffer? = null
    var glUniform: Int? = null

    abstract val size: Long

    override fun dispose() {
        vkUniform?.dispose()
    }
}
