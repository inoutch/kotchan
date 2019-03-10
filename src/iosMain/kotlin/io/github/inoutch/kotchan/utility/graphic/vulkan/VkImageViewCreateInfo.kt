package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkImageViewCreateInfo.copyToNative(
        native: vulkan.VkImageViewCreateInfo) {
    native.sType = VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO
    native.pNext = null
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
