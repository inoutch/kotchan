package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun vulkan.VkPhysicalDeviceProperties.toOrigin(): VkPhysicalDeviceProperties {
    return VkPhysicalDeviceProperties(
            apiVersion.toInt(),
            driverVersion.toInt(),
            vendorID.toInt(),
            deviceID.toInt(),
            VkPhysicalDeviceType.values().find { it.value.toUInt() == deviceType }
                    ?: throw VkNullError("deviceType"),
            deviceName.toKString(),
            List(16) { pipelineCacheUUID[it].toInt() },
            VkPhysicalDeviceLimits(),
            sparseProperties.toOrigin())
}

@ExperimentalUnsignedTypes
actual fun vkGetPhysicalDeviceProperties(physicalDevice: VkPhysicalDevice) = memScoped {
    val native = alloc<vulkan.VkPhysicalDeviceProperties>()

    vulkan.vkGetPhysicalDeviceProperties(physicalDevice.native, native.ptr)

    native.toOrigin()
}
