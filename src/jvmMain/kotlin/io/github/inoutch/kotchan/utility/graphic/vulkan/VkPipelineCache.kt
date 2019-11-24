package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import java.nio.LongBuffer
import org.lwjgl.vulkan.VK10

actual class VkPipelineCache : Disposable {
    var native: Long = 0

    private lateinit var device: VkDevice

    fun init(nativePipelineCache: Long, device: VkDevice) {
        this.native = nativePipelineCache
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyPipelineCache(device.native, native, null)
    }
}

actual fun vkCreatePipelineCache(device: VkDevice, createInfo: VkPipelineCacheCreateInfo) = memScoped {
    val native = LongBuffer.allocate(1)

    checkError(VK10.vkCreatePipelineCache(device.native, createInfo.toNative(this), null, native))

    VkPipelineCache().apply { init(native.get(0), device) }
}
