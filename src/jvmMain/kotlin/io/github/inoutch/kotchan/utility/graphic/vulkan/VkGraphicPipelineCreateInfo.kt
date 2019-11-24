package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkGraphicsPipelineCreateInfo.copyToNative(
    native: org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo,
    memScope: MemScope
) {
    native.sType(VK10.VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .pStages(stages.toNative(memScope))
            .pVertexInputState(vertexInputState.toNative(memScope))
            .pInputAssemblyState(inputAssemblyState.toNative(memScope))
            .pTessellationState(tessellationState?.toNative(memScope))
            .pViewportState(viewportState.toNative(memScope))
            .pRasterizationState(rasterizationState.toNative(memScope))
            .pMultisampleState(multisampleState.toNative(memScope))
            .pDepthStencilState(depthStencilState.toNative(memScope))
            .pColorBlendState(colorBlendState.toNative(memScope))
            .pDynamicState(dynamicState.toNative(memScope))
            .layout(layout.native)
            .renderPass(renderPass.native)
            .subpass(subpass) // index
            .basePipelineHandle(basePipelineHandle?.native ?: VK10.VK_NULL_HANDLE)
            .basePipelineIndex(basePipelineIndex ?: 0)
}

fun List<VkGraphicsPipelineCreateInfo>.toNative(scope: MemScope): org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo.Buffer =
        scope.add(org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } })
