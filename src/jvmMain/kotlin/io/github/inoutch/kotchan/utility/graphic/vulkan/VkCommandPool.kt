package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkCommandPool : Disposable {
    var native: Long = 0
        private set

    lateinit var device: VkDevice

    fun init(nativeCommandPool: Long, device: VkDevice) {
        this.native = nativeCommandPool
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyCommandPool(device.native, native, null)
    }
}

actual fun vkCreateCommandPool(device: VkDevice, createInfo: VkCommandPoolCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateCommandPool(
            device.native,
            createInfo.toNative(this),
            null,
            native))

    VkCommandPool().apply { init(native.get(0), device) }
}
