package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_COMMAND_BUFFER_INHERITANCE_INFO

@ExperimentalUnsignedTypes
fun VkCommandBufferInheritanceInfo.copyToNative(native: vulkan.VkCommandBufferInheritanceInfo) {
    native.sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_INHERITANCE_INFO
    native.pNext = null
    native.renderPass = renderPass.native
    native.subpass = subpass.toUInt()
    native.framebuffer = framebuffer.native
    native.occlusionQueryEnable = if (occlusionQueryEnable) 1u else 0u
    native.queryFlags = queryFlags.sumBy { it.value }.toUInt()
    native.pipelineStatistics = pipelineStatistics.sumBy { it.value }.toUInt()
}

@ExperimentalUnsignedTypes
fun VkCommandBufferInheritanceInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkCommandBufferInheritanceInfo>().also { copyToNative(it) }.ptr
