package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkImage : Disposable {
    lateinit var native: vulkan.VkImage
        private set

    private var device: VkDevice? = null

    fun init(nativeImage: vulkan.VkImage, device: VkDevice? = null) {
        this.native = nativeImage
        this.device = device
    }

    override fun dispose() {
        device?.let { vulkan.vkDestroyImage(it.native, native, null) }
    }
}
