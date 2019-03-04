package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.type.Vector4

actual class VkPipelineColorBlendStateCreateInfo actual constructor(
        logicOpEnable: Boolean,
        logicOp: VkLogicOp,
        attachments: List<VkPipelineColorBlendAttachmentState>,
        blendConstants: Vector4) : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
