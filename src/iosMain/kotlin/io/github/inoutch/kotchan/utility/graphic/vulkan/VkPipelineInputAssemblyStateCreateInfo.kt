package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineInputAssemblyStateCreateInfo.copyToNative(native: vulkan.VkPipelineInputAssemblyStateCreateInfo) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.topology = topology.value.toUInt()
    native.primitiveRestartEnable = if (primitiveRestartEnable) 1u else 0u
}

@ExperimentalUnsignedTypes
fun VkPipelineInputAssemblyStateCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineInputAssemblyStateCreateInfo>()
                .also { copyToNative(it) }.ptr
