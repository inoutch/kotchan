package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkSampler : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeSampler: Long, device: VkDevice) {
        this.native = nativeSampler
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroySampler(device.native, native, null)
    }
}

actual fun vkCreateSampler(device: VkDevice, createInfo: VkSamplerCreateInfo): VkSampler = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateSampler(device.native, createInfo.toNative(this), null, native))

    VkSampler().apply { init(native.get(0), device) }
}
