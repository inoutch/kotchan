package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkSemaphore : Disposable {
    lateinit var native: vulkan.VkSemaphore
        private set

    private lateinit var device: VkDevice

    fun init(nativeSemaphore: vulkan.VkSemaphore, device: VkDevice) {
        this.native = nativeSemaphore
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroySemaphore(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateSemaphore(device: VkDevice, createInfo: VkSemaphoreCreateInfo) = memScoped {
    val native = alloc<vulkan.VkSemaphoreVar>()

    checkError(vulkan.vkCreateSemaphore(device.native, createInfo.toNative(this), null, native.ptr))

    VkSemaphore().apply { init(native.value ?: throw VkNullError("semaphore"), device) }
}
