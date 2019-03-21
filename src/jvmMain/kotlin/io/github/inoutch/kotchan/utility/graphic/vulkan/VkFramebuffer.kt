package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkFramebuffer : Disposable {
    var native: Long = 0
        private set

    lateinit var device: VkDevice

    fun init(nativeFramebuffer: Long, device: VkDevice) {
        this.native = nativeFramebuffer
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyFramebuffer(device.native, native, null)
    }
}

actual fun vkCreateFramebuffer(device: VkDevice, createInfo: VkFramebufferCreateInfo) = memScoped {
    val native = allocLong()

    VK10.vkCreateFramebuffer(device.native, createInfo.toNative(this), null, native)

    VkFramebuffer().apply { init(native.get(0), device) }
}
