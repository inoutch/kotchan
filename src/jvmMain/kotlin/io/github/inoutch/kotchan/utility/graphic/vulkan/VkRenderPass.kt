package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkRenderPass : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeRenderPass: Long, device: VkDevice) {
        this.native = nativeRenderPass
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyRenderPass(device.native, native, null)
    }
}

actual fun vkCreateRenderPass(device: VkDevice, createInfo: VkRenderPassCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateRenderPass(device.native, createInfo.toNative(this), null, native))

    VkRenderPass().apply { init(native.get(0), device) }
}
