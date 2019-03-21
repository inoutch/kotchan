package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkSemaphore : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeSemaphore: Long, device: VkDevice) {
        this.native = nativeSemaphore
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroySemaphore(device.native, native, null)
    }
}

actual fun vkCreateSemaphore(device: VkDevice, createInfo: VkSemaphoreCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateSemaphore(device.native, createInfo.toNative(this), null, native))

    VkSemaphore().apply { init(native.get(0), device) }
}
