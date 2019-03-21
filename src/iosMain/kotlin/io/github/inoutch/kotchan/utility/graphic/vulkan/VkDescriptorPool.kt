package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkDescriptorPool : Disposable {
    lateinit var native: vulkan.VkDescriptorPool
        private set

    private lateinit var device: VkDevice

    fun init(nativeDescriptorPool: vulkan.VkDescriptorPool, device: VkDevice) {
        this.native = nativeDescriptorPool
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyDescriptorPool(device.native, native, null)
    }
}

actual fun vkCreateDescriptorPool(device: VkDevice, createInfo: VkDescriptorPoolCreateInfo) = memScoped {
    val native = alloc<vulkan.VkDescriptorPoolVar>()

    vulkan.vkCreateDescriptorPool(device.native, createInfo.toNative(this), null, native.ptr)

    VkDescriptorPool().apply {
        init(native.value ?: throw VkNullError("descriptorPool"), device)
    }
}
