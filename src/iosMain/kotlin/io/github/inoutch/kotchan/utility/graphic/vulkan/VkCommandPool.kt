package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkCommandPool : Disposable {
    lateinit var native: vulkan.VkCommandPool
        private set

    private lateinit var device: VkDevice

    fun init(nativeCommandPool: vulkan.VkCommandPool, device: VkDevice) {
        this.native = nativeCommandPool
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyCommandPool(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateCommandPool(device: VkDevice, createInfo: VkCommandPoolCreateInfo) = memScoped {
    val native = alloc<vulkan.VkCommandPoolVar>()

    checkError(vulkan.vkCreateCommandPool(device.native, createInfo.toNative(this), null, native.ptr))

    VkCommandPool().apply {
        init(native.value ?: throw VkNullError("commandPool"), device)
    }
}
