package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkCommandBufferInheritanceInfo.copyToNative(native: org.lwjgl.vulkan.VkCommandBufferInheritanceInfo) {
    native.sType(VK10.VK_STRUCTURE_TYPE_COMMAND_BUFFER_INHERITANCE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .renderPass(renderPass.native)
            .subpass(subpass)
            .framebuffer(framebuffer.native)
            .occlusionQueryEnable(occlusionQueryEnable)
            .queryFlags(queryFlags.sumBy { it.value })
            .pipelineStatistics(pipelineStatistics.sumBy { it.value })
}

fun VkCommandBufferInheritanceInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkCommandBufferInheritanceInfo =
        scope.add(org.lwjgl.vulkan.VkCommandBufferInheritanceInfo.calloc()
                .also { copyToNative(it) })
