package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.*

// not disposable because not created
actual class VkPhysicalDevice actual constructor() {
    lateinit var native: vulkan.VkPhysicalDevice
        private set

    fun init(nativePhysicalDevice: vulkan.VkPhysicalDevice) {
        this.native = nativePhysicalDevice
    }
}

@ExperimentalUnsignedTypes
actual fun vkEnumeratePhysicalDevices(instance: VkInstance) = memScoped {
    val count = alloc<UIntVar>()

    checkError(vulkan.vkEnumeratePhysicalDevices(instance.native, count.ptr, null))

    val natives = allocArray<vulkan.VkPhysicalDeviceVar>(count.value.toInt())

    checkError(vulkan.vkEnumeratePhysicalDevices(instance.native, count.ptr, natives))

    List(count.value.toInt()) {
        VkPhysicalDevice().apply { init(natives[it] ?: throw VkNullError("physicalDevice")) }
    }
}
