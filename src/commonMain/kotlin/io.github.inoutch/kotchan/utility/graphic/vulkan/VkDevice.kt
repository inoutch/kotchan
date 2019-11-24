package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkDevice : Disposable

expect fun vkCreateDevice(
    physicalDevice: VkPhysicalDevice,
    createInfo: VkDeviceCreateInfo
): VkDevice

expect fun vkDeviceWaitIdle(device: VkDevice)
