package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
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
        val device = this.device
        if (device != null) {
            VK10.vkDestroyImage(device.native, native, null)
        }
    }
}

