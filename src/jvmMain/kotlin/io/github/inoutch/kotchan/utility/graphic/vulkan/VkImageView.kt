package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkImageView : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeImageView: Long, device: VkDevice) {
        this.native = nativeImageView
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyImageView(device.native, native, null)
    }
}

actual fun vkCreateImageView(device: VkDevice, createInfo: VkImageViewCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateImageView(device.native, createInfo.toNative(this), null, native))

    VkImageView().apply { init(native.get(0), device) }
}
