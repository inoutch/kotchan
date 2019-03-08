package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkFramebuffer : Disposable {
    lateinit var native: vulkan.VkFramebuffer
        private set

    private lateinit var device: VkDevice

    fun init(nativeFramebuffer: vulkan.VkFramebuffer, device: VkDevice) {
        this.native = nativeFramebuffer
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyFramebuffer(device.native, native, null)
    }
}


@ExperimentalUnsignedTypes
actual fun vkCreateFramebuffer(device: VkDevice, createInfo: VkFramebufferCreateInfo) = memScoped {
    val native = alloc<vulkan.VkFramebufferVar>()

    checkError(vulkan.vkCreateFramebuffer(device.native, createInfo.toNative(this), null, native.ptr))

    VkFramebuffer().apply { init(native.value ?: throw VkNullError("framebuffer"), device) }
}
