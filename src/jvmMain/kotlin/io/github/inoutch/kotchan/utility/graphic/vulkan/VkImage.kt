package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkImage : Disposable {
    var native: Long = 0
        private set

    private var device: VkDevice? = null

    fun init(nativeImage: Long, device: VkDevice? = null) {
        this.native = nativeImage
        this.device = device
    }

    override fun dispose() {
        device?.let { VK10.vkDestroyImage(it.native, native, null) }
    }
}

actual fun vkCreateImage(device: VkDevice, createInfo: VkImageCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateImage(device.native, createInfo.toNative(this), null, native))

    VkImage().apply { init(native.get(0), device) }
}

actual fun vkBindImageMemory(device: VkDevice, image: VkImage, memory: VkDeviceMemory, memoryOffset: Long) {
    checkError(VK10.vkBindImageMemory(device.native, image.native, memory.native, memoryOffset))
}
