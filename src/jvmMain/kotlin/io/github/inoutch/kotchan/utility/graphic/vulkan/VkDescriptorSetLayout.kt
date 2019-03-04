package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkDescriptorSetLayout : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeDescriptorSetLayout: Long, device: VkDevice) {
        this.native = nativeDescriptorSetLayout
        this.device = device
    }

    override fun dispose() {
        VK10.vkDestroyDescriptorSetLayout(device.native, native, null)
    }
}

actual fun vkCreateDescriptorSetLayout(
        device: VkDevice,
        createInfo: VkDescriptorSetLayoutCreateInfo): VkDescriptorSetLayout = memScoped {
    val native = allocLong()

    checkError(VK10.vkCreateDescriptorSetLayout(
            device.native,
            createInfo.toNative(this),
            null,
            native))

    VkDescriptorSetLayout().apply { init(native.get(0), device) }
}
