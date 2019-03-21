package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkDescriptorPool : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeDescriptorPool: Long, device: VkDevice) {
        this.native = nativeDescriptorPool
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyDescriptorPool(device.native, native, null)
    }
}

actual fun vkCreateDescriptorPool(device: VkDevice, createInfo: VkDescriptorPoolCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateDescriptorPool(device.native, createInfo.toNative(this), null, native))

    VkDescriptorPool().apply { init(native.get(0), device) }
}
