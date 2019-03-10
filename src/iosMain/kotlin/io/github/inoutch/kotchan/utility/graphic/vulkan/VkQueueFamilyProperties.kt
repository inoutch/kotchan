package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkQueueFamilyProperties.copyToNative(native: vulkan.VkQueueFamilyProperties) {
    native.queueFlags = queueFlags.toUInt()
    native.queueCount = queueCount.toUInt()
    native.timestampValidBits = timestampValidBits.toUInt()
    minImageTransferGranularity.copyToNative(native.minImageTransferGranularity)
}

@ExperimentalUnsignedTypes
fun vulkan.VkQueueFamilyProperties.toOrigin(): VkQueueFamilyProperties {
    return VkQueueFamilyProperties(
            queueFlags.toInt(),
            queueCount.toInt(),
            timestampValidBits.toInt(),
            minImageTransferGranularity.toOrigin())
}

@ExperimentalUnsignedTypes
actual fun vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice: VkPhysicalDevice) = memScoped {
    val count = alloc<UIntVar>()
    vulkan.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice.native, count.ptr, null)

    val properties = allocArray<vulkan.VkQueueFamilyProperties>(count.value.toInt())
    vulkan.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice.native, count.ptr, properties)

    List(count.value.toInt()) { properties[it].toOrigin() }
}
