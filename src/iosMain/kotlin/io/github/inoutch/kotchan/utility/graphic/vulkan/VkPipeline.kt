package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*
import vulkan.VkPipelineVar

actual class VkPipeline : Disposable {
    lateinit var native: vulkan.VkPipeline
        private set

    private lateinit var device: VkDevice

    fun init(nativePipeline: vulkan.VkPipeline, device: VkDevice) {
        this.native = nativePipeline
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyPipeline(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateGraphicsPipelines(
    device: VkDevice,
    pipelineCache: VkPipelineCache?,
    createInfos: List<VkGraphicsPipelineCreateInfo>
) = memScoped {
    val native = alloc<VkPipelineVar>()

    checkError(vulkan.vkCreateGraphicsPipelines(
            device.native,
            pipelineCache?.native,
            createInfos.size.toUInt(),
            createInfos.toNative(this),
            null,
            native.ptr))

    VkPipeline().apply { init(native.value ?: throw VkNullError("pipeline"), device) }
}
