package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkPipelineMultisampleStateCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo,
        scope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .rasterizationSamples(rasterizationSamples.sumBy { it.value })
            .sampleShadingEnable(sampleShadingEnable)
            .minSampleShading(minSampleShading)
            .pSampleMask(sampleMask?.let {
                val x = scope.allocInt()
                x.put(it)
                x.flip()
                x
            })
            .alphaToCoverageEnable(alphaToCoverageEnable)
            .alphaToOneEnable(alphaToOneEnable)
}

fun VkPipelineMultisampleStateCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo =
        scope.add(org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo.calloc()
                .also { copyToNative(it, scope) })
