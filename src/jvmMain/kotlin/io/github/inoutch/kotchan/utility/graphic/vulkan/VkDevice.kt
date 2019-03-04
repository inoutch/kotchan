package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkDevice : Disposable {
    lateinit var native: org.lwjgl.vulkan.VkDevice
        private set

    fun init(nativeDevice: org.lwjgl.vulkan.VkDevice) {
        this.native = nativeDevice
    }

    override fun dispose() {
        VK10.vkDestroyDevice(native, null)
    }
}

actual fun vkCreateDevice(
        physicalDevice: VkPhysicalDevice,
        createInfo: VkDeviceCreateInfo) = memScoped {
    val nativeCreateInfo = createInfo.toNative(this)
    val native = allocPointer()

    checkError(VK10.vkCreateDevice(physicalDevice.native, nativeCreateInfo, null, native))

    VkDevice().apply {
        init(org.lwjgl.vulkan.VkDevice(native.get(0), physicalDevice.native, nativeCreateInfo))
    }
}
