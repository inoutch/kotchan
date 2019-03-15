package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkImage : Disposable {
    lateinit var native: vulkan.VkImage
        private set

    private var device: VkDevice? = null

    fun init(nativeImage: vulkan.VkImage, device: VkDevice? = null) {
        this.native = nativeImage
        this.device = device
    }

    override fun dispose() {
        device?.let { vulkan.vkDestroyImage(it.native, native, null) }
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateImage(device: VkDevice, createInfo: VkImageCreateInfo) = memScoped {
    val native = alloc<vulkan.VkImageVar>()

    checkError(vulkan.vkCreateImage(device.native, createInfo.toNative(this), null, native.ptr))

    VkImage().apply { init(native.value ?: throw VkNullError("image"), device) }
}

@ExperimentalUnsignedTypes
actual fun vkBindImageMemory(device: VkDevice, image: VkImage, memory: VkDeviceMemory, memoryOffset: Long) {
    vulkan.vkBindImageMemory(device.native, image.native, memory.native, memoryOffset.toULong())
}
