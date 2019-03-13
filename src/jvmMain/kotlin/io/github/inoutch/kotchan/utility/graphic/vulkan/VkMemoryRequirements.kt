package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

fun org.lwjgl.vulkan.VkMemoryRequirements.toOrigin() =
        VkMemoryRequirements(size(), alignment(), memoryTypeBits())

actual fun vkGetBufferMemoryRequirements(device: VkDevice, buffer: VkBuffer) = memScoped {
    val native = org.lwjgl.vulkan.VkMemoryRequirements.calloc()

    VK10.vkGetBufferMemoryRequirements(device.native, buffer.native, native)

    native.toOrigin()
}

actual fun vkGetImageMemoryRequirements(device: VkDevice, image: VkImage): VkMemoryRequirements = memScoped {
    val native = org.lwjgl.vulkan.VkMemoryRequirements.calloc()

    VK10.vkGetImageMemoryRequirements(device.native, image.native, native)

    native.toOrigin()
}
