package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkMemoryAllocateInfo.copyToNative(native: org.lwjgl.vulkan.VkMemoryAllocateInfo) {
    native.sType(VK10.VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .allocationSize(allocationSize)
            .memoryTypeIndex(memoryTypeIndex)
}

fun VkMemoryAllocateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkMemoryAllocateInfo =
        scope.add(org.lwjgl.vulkan.VkMemoryAllocateInfo.calloc()
                .also { copyToNative(it) })
