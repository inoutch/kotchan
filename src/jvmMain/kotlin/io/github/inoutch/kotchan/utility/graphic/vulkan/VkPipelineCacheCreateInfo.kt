package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkPipelineCacheCreateInfo.copyToNative(
    native: org.lwjgl.vulkan.VkPipelineCacheCreateInfo,
    scope: MemScope
) {
    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_CACHE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .pInitialData(initialData.toNative(scope))
}

fun VkPipelineCacheCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkPipelineCacheCreateInfo =
        scope.add(org.lwjgl.vulkan.VkPipelineCacheCreateInfo.calloc()
                .also { copyToNative(it, scope) })
