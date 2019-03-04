package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkPipelineRasterizationStateCreateInfo actual constructor(polygonMode: VkPolygonMode, cullMode: VkCullMode, frontFace: VkFrontFace, depthClampEnable: Boolean, rasterizerDiscardEnable: Boolean, depthBiasEnable: Boolean) : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
