package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkBuffer : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeBuffer: Long, device: VkDevice) {
        this.native = nativeBuffer
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyBuffer(device.native, native, null)
    }
}

actual fun vkCreateBuffer(device: VkDevice, createInfo: VkBufferCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateBuffer(device.native, createInfo.toNative(this), null, native))

    VkBuffer().apply { init(native.get(0), device) }
}
