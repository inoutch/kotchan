package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkPipeline : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativePipeline: Long, device: VkDevice) {
        this.native = nativePipeline
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyPipeline(device.native, native, null)
    }
}

actual fun vkCreateGraphicsPipelines(
    device: VkDevice,
    pipelineCache: VkPipelineCache?,
    createInfos: List<VkGraphicsPipelineCreateInfo>
) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateGraphicsPipelines(
            device.native,
            pipelineCache?.native ?: VK10.VK_NULL_HANDLE,
            createInfos.toNative(this),
            null,
            native))

    VkPipeline().apply { init(native.get(0), device) }
}
