package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineRasterizationStateCreateInfo.copyToNative(native: vulkan.VkPipelineRasterizationStateCreateInfo) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.polygonMode = polygonMode.value.toUInt()
    native.cullMode = cullMode.value.toUInt()
    native.frontFace = frontFace.value.toUInt()
    native.depthClampEnable = depthClampEnable.toVkBool32()
    native.rasterizerDiscardEnable = rasterizerDiscardEnable.toVkBool32()
    native.depthBiasEnable = depthBiasEnable.toVkBool32()
    native.depthBiasConstantFactor = depthBiasConstantFactor
    native.depthBiasClamp = depthBiasClamp
    native.depthBiasSlopeFactor = depthBiasSlopeFactor
    native.lineWidth = lineWidth
}

@ExperimentalUnsignedTypes
fun VkPipelineRasterizationStateCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineRasterizationStateCreateInfo>()
                .also { copyToNative(it) }.ptr
