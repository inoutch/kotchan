package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*
import vulkan.VkDeviceVar

actual class VkDevice : Disposable {
    lateinit var native: vulkan.VkDevice
        private set

    fun init(nativeDevice: vulkan.VkDevice) {
        this.native = nativeDevice
    }

    override fun dispose() {
        vulkan.vkDestroyDevice(native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateDevice(physicalDevice: VkPhysicalDevice, createInfo: VkDeviceCreateInfo) = memScoped {
    val native = alloc<VkDeviceVar>()

    checkError(vulkan.vkCreateDevice(physicalDevice.native, createInfo.toNative(this), null, native.ptr))

    VkDevice().apply { init(native.value ?: throw VkNullError("device")) }
}

actual fun vkDeviceWaitIdle(device: VkDevice) {
    checkError(vulkan.vkDeviceWaitIdle(device.native))
}
