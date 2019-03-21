package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*
import vulkan.VkBufferVar

actual class VkBuffer : Disposable {
    lateinit var native: vulkan.VkBuffer
        private set

    private lateinit var device: VkDevice

    fun init(nativeBuffer: vulkan.VkBuffer, device: VkDevice) {
        this.native = nativeBuffer
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyBuffer(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateBuffer(device: VkDevice, createInfo: VkBufferCreateInfo) = memScoped {
    val native = alloc<VkBufferVar>()

    checkError(vulkan.vkCreateBuffer(device.native, createInfo.toNative(this), null, native.ptr))

    VkBuffer().apply { init(native.value ?: throw VkNullError("buffer"), device) }
}
