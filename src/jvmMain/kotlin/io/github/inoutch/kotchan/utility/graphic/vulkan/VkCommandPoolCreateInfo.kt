package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkCommandPoolCreateInfo.copyToNative(native: org.lwjgl.vulkan.VkCommandPoolCreateInfo) {
    native.sType(VK10.VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .queueFamilyIndex(queueFamilyIndex)
}

fun VkCommandPoolCreateInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkCommandPoolCreateInfo =
        memScope.add(org.lwjgl.vulkan.VkCommandPoolCreateInfo.calloc()
                .also { copyToNative(it) })
