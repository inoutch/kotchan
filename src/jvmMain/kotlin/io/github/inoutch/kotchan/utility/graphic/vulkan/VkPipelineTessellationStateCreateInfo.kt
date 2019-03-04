package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkPipelineTessellationStateCreateInfo.copyToNative(native: org.lwjgl.vulkan.VkPipelineTessellationStateCreateInfo) {
    native.flags(flags)
            .patchControlPoints(patchControlPoints)
}

fun VkPipelineTessellationStateCreateInfo.toNative(scope: MemScope)
        : org.lwjgl.vulkan.VkPipelineTessellationStateCreateInfo =
        scope.add(org.lwjgl.vulkan.VkPipelineTessellationStateCreateInfo.calloc()
                .also { copyToNative(it) })
