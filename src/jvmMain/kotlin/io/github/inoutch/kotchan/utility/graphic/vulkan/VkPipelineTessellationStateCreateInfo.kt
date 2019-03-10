package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkPipelineTessellationStateCreateInfo.copyToNative(native: org.lwjgl.vulkan.VkPipelineTessellationStateCreateInfo) {
    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_TESSELLATION_STATE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .patchControlPoints(patchControlPoints)
}

fun VkPipelineTessellationStateCreateInfo.toNative(scope: MemScope)
        : org.lwjgl.vulkan.VkPipelineTessellationStateCreateInfo =
        scope.add(org.lwjgl.vulkan.VkPipelineTessellationStateCreateInfo.calloc()
                .also { copyToNative(it) })
