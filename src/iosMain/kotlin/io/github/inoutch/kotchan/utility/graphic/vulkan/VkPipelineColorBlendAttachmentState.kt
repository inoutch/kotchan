package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkPipelineColorBlendAttachmentState.copyToNative(native: vulkan.VkPipelineColorBlendAttachmentState) {
    native.blendEnable = blendEnable.toVkBool32()
    native.srcColorBlendFactor = srcColorBlendFactor.value.toUInt()
    native.dstColorBlendFactor = dstColorBlendFactor.value.toUInt()
    native.colorBlendOp = colorBlendOp.value.toUInt()
    native.srcAlphaBlendFactor = srcAlphaBlendFactor.value.toUInt()
    native.dstAlphaBlendFactor = dstAlphaBlendFactor.value.toUInt()
    native.alphaBlendOp = alphaBlendOp.value.toUInt()
    native.colorWriteMask = colorWriteMask.sumBy { it.value }.toUInt()
}

@ExperimentalUnsignedTypes
fun VkPipelineColorBlendAttachmentState.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineColorBlendAttachmentState>()
                .also { copyToNative(it) }.ptr

@ExperimentalUnsignedTypes
fun List<VkPipelineColorBlendAttachmentState>.toNative(scope: MemScope) =
        scope.allocArray<vulkan.VkPipelineColorBlendAttachmentState>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
