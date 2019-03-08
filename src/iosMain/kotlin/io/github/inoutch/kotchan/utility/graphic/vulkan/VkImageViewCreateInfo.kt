package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkImageViewCreateInfo.copyToNative(
        native: vulkan.VkImageViewCreateInfo) {
    native.flags = flags.toUInt()
    native.image = image.native
    native.viewType = viewType.value.toUInt()
    native.format = format.value.toUInt()
    components.copyToNative(native.components)
    subresourceRange.copyToNative(native.subresourceRange)
}

@ExperimentalUnsignedTypes
fun VkImageViewCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkImageViewCreateInfo>()
                .also { copyToNative(it) }.ptr
