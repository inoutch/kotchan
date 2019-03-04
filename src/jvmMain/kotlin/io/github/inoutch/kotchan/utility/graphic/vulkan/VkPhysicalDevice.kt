package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkPhysicalDevice {

    lateinit var native: org.lwjgl.vulkan.VkPhysicalDevice
        private set

    fun init(nativePhysicalDevice: org.lwjgl.vulkan.VkPhysicalDevice) {
        this.native = nativePhysicalDevice
    }
}

actual fun vkEnumeratePhysicalDevices(instance: VkInstance) = memScoped {
    val physicalDeviceCount = allocInt()
    checkError(VK10.vkEnumeratePhysicalDevices(instance.native, physicalDeviceCount, null))

    val native = allocPointer(physicalDeviceCount.get(0))
    checkError(VK10.vkEnumeratePhysicalDevices(instance.native, physicalDeviceCount, native))

    List(physicalDeviceCount.get(0)) {
        VkPhysicalDevice().apply { init(org.lwjgl.vulkan.VkPhysicalDevice(native.get(it), instance.native)) }
    }
}
