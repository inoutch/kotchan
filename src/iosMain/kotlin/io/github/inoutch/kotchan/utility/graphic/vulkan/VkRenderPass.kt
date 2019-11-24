package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*
import vulkan.VkRenderPassVar

actual class VkRenderPass : Disposable {
    lateinit var native: vulkan.VkRenderPass

    private lateinit var device: VkDevice

    fun init(nativeRenderPass: vulkan.VkRenderPass, device: VkDevice) {
        this.native = nativeRenderPass
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyRenderPass(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateRenderPass(device: VkDevice, createInfo: VkRenderPassCreateInfo) = memScoped {
    val native = alloc<VkRenderPassVar>()

    checkError(vulkan.vkCreateRenderPass(device.native, createInfo.toNative(this), null, native.ptr))

    VkRenderPass().apply { init(native.value ?: throw VkNullError("renderPass"), device) }
}
