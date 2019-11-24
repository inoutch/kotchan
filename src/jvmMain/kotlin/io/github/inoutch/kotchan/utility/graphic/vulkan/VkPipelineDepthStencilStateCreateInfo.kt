package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkPipelineDepthStencilStateCreateInfo.copyToNative(
    native: org.lwjgl.vulkan.VkPipelineDepthStencilStateCreateInfo,
    scope: MemScope
) {

    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_DEPTH_STENCIL_STATE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
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
