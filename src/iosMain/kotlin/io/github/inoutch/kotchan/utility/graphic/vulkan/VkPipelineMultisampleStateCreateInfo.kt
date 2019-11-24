package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineMultisampleStateCreateInfo.copyToNative(
    native: vulkan.VkPipelineMultisampleStateCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.rasterizationSamples = rasterizationSamples.sumBy { it.value }.toUInt()
    native.sampleShadingEnable = sampleShadingEnable.toVkBool32()
    native.minSampleShading = minSampleShading
    native.pSampleMask = sampleMask?.let { listOf(it.toUInt()) }?.toNative(scope)
    native.alphaToCoverageEnable = alphaToCoverageEnable.toVkBool32()
    native.alphaToOneEnable = alphaToOneEnable.toVkBool32()
}

@ExperimentalUnsignedTypes
fun VkPipelineMultisampleStateCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineMultisampleStateCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
