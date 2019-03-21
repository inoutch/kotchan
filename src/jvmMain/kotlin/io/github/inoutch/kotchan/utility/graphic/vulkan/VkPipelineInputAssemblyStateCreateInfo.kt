package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkPipelineInputAssemblyStateCreateInfo.copyToNative(native: org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo) {
    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .topology(topology.value)
            .primitiveRestartEnable(primitiveRestartEnable)
}

fun VkPipelineInputAssemblyStateCreateInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo =
        memScope.add(org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo.calloc()
                .also { copyToNative(it) })
