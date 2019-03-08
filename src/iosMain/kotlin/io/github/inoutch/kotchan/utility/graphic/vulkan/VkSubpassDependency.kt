package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkSubpassDependency.copyToNative(native: vulkan.VkSubpassDependency) {
    native.srcSubpass = srcSubpassIndex.toUInt()
    native.dstSubpass = dstSubpassIndex.toUInt()
    native.srcStageMask = srcStageMask.value.toUInt()
    native.dstStageMask = dstStageMask.value.toUInt()
    native.srcAccessMask = srcAccessMask.value.toUInt()
    native.dstAccessMask = dstAccessMask.value.toUInt()
    native.dependencyFlags = dependencyFlags.sumBy { it.value }.toUInt()
}

@ExperimentalUnsignedTypes
fun VkSubpassDependency.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkSubpassDependency>().also { copyToNative(it) }

@ExperimentalUnsignedTypes
fun List<VkSubpassDependency>.toNative(scope: MemScope) =
        scope.allocArray<vulkan.VkSubpassDependency>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
