package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkPipelineColorBlendAttachmentState actual constructor(
        blendEnable: Boolean,
        srcColorBlendFactor: VkBlendFactor,
        dstColorBlendFactor: VkBlendFactor,
        colorBlendOp: VkBlendOp,
        srcAlphaBlendFactor: VkBlendFactor,
        dstAlphaBlendFactor: VkBlendFactor,
        alphaBlendOp: VkBlendOp,
        colorWriteMask: List<VkColorComponentFlagBits>) : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
