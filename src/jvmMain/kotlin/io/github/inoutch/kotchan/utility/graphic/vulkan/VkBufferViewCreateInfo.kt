package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkBufferViewCreateInfo.copyToNative(native: org.lwjgl.vulkan.VkBufferViewCreateInfo) {
    native.sType(VK10.VK_STRUCTURE_TYPE_BUFFER_VIEW_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags.sumBy { it.value })
            .buffer(buffer.native)
            .format(format.value)
            .offset(offset)
            .range(range)
}

fun VkBufferViewCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkBufferViewCreateInfo =
        scope.add(org.lwjgl.vulkan.VkBufferViewCreateInfo.calloc()
                .also { copyToNative(it) })
