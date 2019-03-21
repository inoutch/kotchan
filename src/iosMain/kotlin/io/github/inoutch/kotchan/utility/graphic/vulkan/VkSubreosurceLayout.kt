package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun vulkan.VkSubresourceLayout.toOrigin(): VkSubresourceLayout {
    return VkSubresourceLayout(
            offset.toLong(),
            size.toLong(),
            rowPitch.toLong(),
            arrayPitch.toLong(),
            depthPitch.toLong())
}

@ExperimentalUnsignedTypes
actual fun vkGetImageSubresourceLayout(device: VkDevice, image: VkImage, subresource: VkImageSubresource) = memScoped {
    val native = alloc<vulkan.VkSubresourceLayout>()
    vulkan.vkGetImageSubresourceLayout(device.native, image.native, subresource.toNative(this), native.ptr)

    native.toOrigin()
}
