package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkPipelineColorBlendStateCreateInfo.copyToNative(
    native: org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo,
    memScope: MemScope
) {
    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .logicOpEnable(logicOpEnable)
            .logicOp(logicOp.value)
            .pAttachments(attachments.toNative(memScope))
            .blendConstants(blendConstants.flatten().toNative(memScope))
}

fun VkPipelineColorBlendStateCreateInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo =
        memScope.add(org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo.calloc()
                .also { copyToNative(it, memScope) })
