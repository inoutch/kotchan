package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkCommandBufferBeginInfo.copyToNative(
        native: org.lwjgl.vulkan.VkCommandBufferBeginInfo,
        scope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO)
            .flags(flags.sumBy { it.value })
            .pInheritanceInfo(inheritanceInfo?.toNative(scope))
}

fun VkCommandBufferBeginInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkCommandBufferBeginInfo =
        scope.add(org.lwjgl.vulkan.VkCommandBufferBeginInfo.calloc()
                .also { copyToNative(it, scope) })
