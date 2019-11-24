package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineColorBlendStateCreateInfo.copyToNative(
    native: vulkan.VkPipelineColorBlendStateCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.logicOpEnable = logicOpEnable.toVkBool32()
    native.logicOp = logicOp.value.toUInt()
    native.attachmentCount = attachments.size.toUInt()
    native.pAttachments = attachments.toNative(scope)
    native.blendConstants[0] = blendConstants.x
    native.blendConstants[1] = blendConstants.y
    native.blendConstants[2] = blendConstants.z
    native.blendConstants[3] = blendConstants.w
}

@ExperimentalUnsignedTypes
fun VkPipelineColorBlendStateCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineColorBlendStateCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
