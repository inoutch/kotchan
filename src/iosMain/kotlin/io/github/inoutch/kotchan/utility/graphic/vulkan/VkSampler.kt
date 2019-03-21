package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkSampler : Disposable {
    lateinit var native: vulkan.VkSampler
        private set

    private lateinit var device: VkDevice

    fun init(nativeSampler: vulkan.VkSampler, device: VkDevice) {
        this.native = nativeSampler
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroySampler(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateSampler(device: VkDevice, createInfo: VkSamplerCreateInfo) = memScoped {
    val native = alloc<vulkan.VkSamplerVar>()

    checkError(vulkan.vkCreateSampler(device.native, createInfo.toNative(this), null, native.ptr))

    VkSampler().apply { init(native.value ?: throw VkNullError("sampler"), device) }
}
