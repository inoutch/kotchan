package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkPipelineDepthStencilStateCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkPipelineDepthStencilStateCreateInfo,
        scope: MemScope) {

    native.flags(flags)
            .depthTestEnable(depthTestEnable)
            .depthWriteEnable(depthWriteEnable)
            .depthCompareOp(depthCompareOp.value)
            .depthBoundsTestEnable(depthBoundsTestEnable)
            .stencilTestEnable(stencilTestEnable)
            .front(front.toNative(scope))
            .back(back.toNative(scope))
            .minDepthBounds(minDepthBounds)
            .maxDepthBounds(maxDepthBounds)
}

fun VkPipelineDepthStencilStateCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkPipelineDepthStencilStateCreateInfo =
        scope.add(org.lwjgl.vulkan.VkPipelineDepthStencilStateCreateInfo.calloc()
                .also { copyToNative(it, scope) })
