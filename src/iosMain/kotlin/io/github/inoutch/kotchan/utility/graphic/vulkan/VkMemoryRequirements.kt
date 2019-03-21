package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr

@ExperimentalUnsignedTypes
fun vulkan.VkMemoryRequirements.toOrigin(): VkMemoryRequirements {
    return VkMemoryRequirements(size.toLong(), alignment.toLong(), memoryTypeBits.toInt())
}

@ExperimentalUnsignedTypes
actual fun vkGetBufferMemoryRequirements(device: VkDevice, buffer: VkBuffer) = memScoped {
    val native = alloc<vulkan.VkMemoryRequirements>()

    vulkan.vkGetBufferMemoryRequirements(device.native, buffer.native, native.ptr)

    native.toOrigin()
}

@ExperimentalUnsignedTypes
actual fun vkGetImageMemoryRequirements(device: VkDevice, image: VkImage) = memScoped {
    val native = alloc<vulkan.VkMemoryRequirements>()

    vulkan.vkGetImageMemoryRequirements(device.native, image.native, native.ptr)

    native.toOrigin()
}
