package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkBufferView : Disposable {
    lateinit var native: vulkan.VkBufferView
        private set

    private lateinit var device: VkDevice

    fun init(nativeBufferView: vulkan.VkBufferView, device: VkDevice) {
        this.native = nativeBufferView
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyBufferView(device.native, native, null)
    }
}

actual fun vkCreateBufferView(device: VkDevice, createInfo: VkBufferViewCreateInfo) = memScoped {
    val native = alloc<vulkan.VkBufferViewVar>()

    checkError(vulkan.vkCreateBufferView(device.native, createInfo.toNative(this), null, native.ptr))

    VkBufferView().apply {
        init(native.value ?: throw VkNullError("bufferView"), device)
    }
}
