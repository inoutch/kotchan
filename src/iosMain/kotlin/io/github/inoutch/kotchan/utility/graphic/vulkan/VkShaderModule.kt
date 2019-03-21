package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkShaderModule : Disposable {
    lateinit var native: vulkan.VkShaderModule
        private set

    private lateinit var device: VkDevice

    fun init(nativeShaderModule: vulkan.VkShaderModule, device: VkDevice) {
        this.native = nativeShaderModule
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyShaderModule(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateShaderModule(device: VkDevice, shaderModuleCreateInfo: VkShaderModuleCreateInfo) = memScoped {
    val native = alloc<vulkan.VkShaderModuleVar>()

    checkError(vulkan.vkCreateShaderModule(device.native, shaderModuleCreateInfo.toNative(this), null, native.ptr))

    VkShaderModule().apply { init(native.value ?: throw VkNullError("shaderModule"), device) }
}
