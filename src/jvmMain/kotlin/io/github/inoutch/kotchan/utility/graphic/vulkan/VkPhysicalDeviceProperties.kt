package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

fun org.lwjgl.vulkan.VkPhysicalDeviceProperties.toOrigin(): VkPhysicalDeviceProperties {
    return VkPhysicalDeviceProperties(
            apiVersion(),
            driverVersion(),
            vendorID(),
            deviceID(),
            VkPhysicalDeviceType.values().find { it.value == deviceType() }
                    ?: throw VkNullError("deviceType"),
            deviceName().toString(),
            pipelineCacheUUID().asIntBuffer().array().asList(),
            VkPhysicalDeviceLimits(),
            VkPhysicalDeviceSparseProperties(
                    sparseProperties().residencyStandard2DBlockShape(),
                    sparseProperties().residencyStandard2DMultisampleBlockShape(),
                    sparseProperties().residencyStandard3DBlockShape(),
                    sparseProperties().residencyAlignedMipSize(),
                    sparseProperties().residencyNonResidentStrict()))
}

actual fun vkGetPhysicalDeviceProperties(physicalDevice: VkPhysicalDevice) = memScoped {
    val native = add(org.lwjgl.vulkan.VkPhysicalDeviceProperties.calloc())

    VK10.vkGetPhysicalDeviceProperties(physicalDevice.native, native)

    native.toOrigin()
}
