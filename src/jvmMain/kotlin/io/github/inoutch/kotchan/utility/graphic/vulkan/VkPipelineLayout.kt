package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkPipelineLayout : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativePipelineLayout: Long, device: VkDevice) {
        this.native = nativePipelineLayout
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyPipelineLayout(device.native, native, null)
    }
}

actual fun vkCreatePipelineLayout(device: VkDevice, createInfo: VkPipelineLayoutCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreatePipelineLayout(device.native, createInfo.toNative(this), null, native))

    VkPipelineLayout().apply { init(native.get(0), device) }
}
