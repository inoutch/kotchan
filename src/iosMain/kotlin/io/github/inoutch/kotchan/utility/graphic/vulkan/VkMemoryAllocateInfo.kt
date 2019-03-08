package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO

@ExperimentalUnsignedTypes
fun VkMemoryAllocateInfo.copyToNative(native: vulkan.VkMemoryAllocateInfo) {
    native.sType = VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO
    native.pNext = null
    native.allocationSize = allocationSize.toULong()
    native.memoryTypeIndex = memoryTypeIndex.toUInt()
}

@ExperimentalUnsignedTypes
fun VkMemoryAllocateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkMemoryAllocateInfo>()
                .also { copyToNative(it) }.ptr
