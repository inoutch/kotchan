package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkPipelineLayout : Disposable {
    lateinit var native: vulkan.VkPipelineLayout
        private set

    private lateinit var device: VkDevice

    fun init(nativePipelineLayout: vulkan.VkPipelineLayout, device: VkDevice) {
        this.native = nativePipelineLayout
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyPipelineLayout(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreatePipelineLayout(device: VkDevice, createInfo: VkPipelineLayoutCreateInfo) = memScoped {
    val native = alloc<vulkan.VkPipelineLayoutVar>()

    checkError(vulkan.vkCreatePipelineLayout(device.native, createInfo.toNative(this), null, native.ptr))

    VkPipelineLayout().apply { init(native.value ?: throw VkNullError("pipelineLayout"), device) }
}
