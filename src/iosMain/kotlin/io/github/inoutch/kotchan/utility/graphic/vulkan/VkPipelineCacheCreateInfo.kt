package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_CACHE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineCacheCreateInfo.copyToNative(
    native: vulkan.VkPipelineCacheCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_CACHE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.initialDataSize = initialDataSize.toULong()
    native.pInitialData = initialData.refTo(0).getPointer(scope).rawValue.toLong().toCPointer()
}

@ExperimentalUnsignedTypes
fun VkPipelineCacheCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPipelineCacheCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
