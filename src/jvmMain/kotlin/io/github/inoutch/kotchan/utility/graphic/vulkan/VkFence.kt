package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkFence : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeFence: Long, device: VkDevice) {
        this.native = nativeFence
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyFence(device.native, native, null)
    }
}

actual fun vkCreateFence(device: VkDevice, createInfo: VkFenceCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateFence(device.native, createInfo.toNative(this), null, native))

    VkFence().apply { init(native.get(0), device) }
}
