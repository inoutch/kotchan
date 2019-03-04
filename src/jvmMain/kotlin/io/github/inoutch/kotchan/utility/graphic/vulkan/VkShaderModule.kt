package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkShaderModule : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeSurface: Long, device: VkDevice) {
        this.native = nativeSurface
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyShaderModule(device.native, native, null)
    }
}

actual fun vkCreateShaderModule(device: VkDevice, shaderModuleCreateInfo: VkShaderModuleCreateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateShaderModule(device.native, shaderModuleCreateInfo.toNative(this), null, native))

    VkShaderModule().apply { init(native.get(0), device) }
}
