package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkSemaphoreCreateInfo.copyToNative(native: org.lwjgl.vulkan.VkSemaphoreCreateInfo) {
    native.sType(VK10.VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
}

fun VkSemaphoreCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkSemaphoreCreateInfo =
        scope.add(org.lwjgl.vulkan.VkSemaphoreCreateInfo.calloc()
                .also { copyToNative(it) })
