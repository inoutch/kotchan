package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkBufferView : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeBufferView: Long, device: VkDevice) {
        this.native = nativeBufferView
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyBufferView(device.native, native, null)
    }
}

actual fun vkCreateBufferView(device: VkDevice, createInfo: VkBufferViewCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateBufferView(device.native, createInfo.toNative(this), null, native))

    VkBufferView().apply { init(native.get(0), device) }
}
