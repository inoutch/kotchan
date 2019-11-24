package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

fun org.lwjgl.vulkan.VkSubresourceLayout.toOrigin(): VkSubresourceLayout {
    return VkSubresourceLayout(offset(), size(), rowPitch(), arrayPitch(), depthPitch())
}

actual fun vkGetImageSubresourceLayout(
    device: VkDevice,
    image: VkImage,
    subresource: VkImageSubresource
) = memScoped {
    val native = add(org.lwjgl.vulkan.VkSubresourceLayout.calloc())

    VK10.vkGetImageSubresourceLayout(device.native, image.native, subresource.toNative(this), native)

    native.toOrigin()
}
