package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkPipelineCache : Disposable {
    lateinit var native: vulkan.VkPipelineCache
        private set

    private lateinit var device: VkDevice

    fun init(nativePipelineCache: vulkan.VkPipelineCache, device: VkDevice) {
        this.native = nativePipelineCache
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyPipelineCache(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreatePipelineCache(device: VkDevice, createInfo: VkPipelineCacheCreateInfo) = memScoped {
    val native = alloc<vulkan.VkPipelineCacheVar>()

    checkError(vulkan.vkCreatePipelineCache(device.native, createInfo.toNative(this), null, native.ptr))

    VkPipelineCache().apply { init(native.value ?: throw VkNullError("pipelineCache"), device) }
}
