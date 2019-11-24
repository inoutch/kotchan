package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkGraphicsPipelineCreateInfo.copyToNative(
    native: vulkan.VkGraphicsPipelineCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.stageCount = stages.size.toUInt()
    native.pStages = stages.toNative(scope)
    native.pVertexInputState = vertexInputState.toNative(scope)
    native.pInputAssemblyState = inputAssemblyState.toNative(scope)
    native.pTessellationState = tessellationState?.toNative(scope)
    native.pViewportState = viewportState.toNative(scope)
    native.pRasterizationState = rasterizationState.toNative(scope)
    native.pMultisampleState = multisampleState.toNative(scope)
    native.pDepthStencilState = depthStencilState.toNative(scope)
    native.pColorBlendState = colorBlendState.toNative(scope)
    native.pDynamicState = dynamicState.toNative(scope)
    native.layout = layout.native
    native.renderPass = renderPass.native
    native.subpass = subpass.toUInt()
    native.basePipelineHandle = basePipelineHandle?.native
    native.basePipelineIndex = basePipelineIndex ?: 0
}

@ExperimentalUnsignedTypes
fun VkGraphicsPipelineCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkGraphicsPipelineCreateInfo>().also { copyToNative(it, scope) }.ptr

@ExperimentalUnsignedTypes
fun List<VkGraphicsPipelineCreateInfo>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkGraphicsPipelineCreateInfo>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } }
