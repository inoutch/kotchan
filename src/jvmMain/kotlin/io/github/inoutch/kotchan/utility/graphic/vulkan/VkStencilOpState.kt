package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkStencilOpState.copyToNative(native: org.lwjgl.vulkan.VkStencilOpState) {
    native.failOp(failOp.value)
            .passOp(passOp.value)
            .depthFailOp(depthFailOp.value)
            .compareOp(compareOp.value)
            .compareMask(compareMask)
            .writeMask(writeMask)
            .reference(reference)
}

fun VkStencilOpState.toNative(scope: MemScope): org.lwjgl.vulkan.VkStencilOpState =
        scope.add(org.lwjgl.vulkan.VkStencilOpState.calloc()
                .also { copyToNative(it) })
