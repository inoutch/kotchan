package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkPipelineColorBlendAttachmentState.copyToNative(native: org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState) {
    native.blendEnable(blendEnable)
            .srcColorBlendFactor(srcColorBlendFactor.value)
            .dstColorBlendFactor(dstColorBlendFactor.value)
            .colorBlendOp(colorBlendOp.value)
            .srcAlphaBlendFactor(srcAlphaBlendFactor.value)
            .dstAlphaBlendFactor(dstAlphaBlendFactor.value)
            .alphaBlendOp(alphaBlendOp.value)
            .colorWriteMask(colorWriteMask.sumBy { it.value })
}

fun VkPipelineColorBlendAttachmentState.toNative(memScope: MemScope): org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState =
        memScope.add(org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState.calloc()
                .also { copyToNative(it) })

fun List<VkPipelineColorBlendAttachmentState>.toNative(memScope: MemScope) =
        memScope.add(org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
