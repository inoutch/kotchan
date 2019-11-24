package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.type.Vector4

data class VkPipelineColorBlendStateCreateInfo(
    val flags: Int,
    val logicOpEnable: Boolean,
    val logicOp: VkLogicOp,
    val attachments: List<VkPipelineColorBlendAttachmentState>,
    val blendConstants: Vector4
)
