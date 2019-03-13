package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkImage : Disposable

expect fun vkCreateImage(device: VkDevice, createInfo: VkImageCreateInfo): VkImage

expect fun vkBindImageMemory(device: VkDevice, image: VkImage, memory: VkDeviceMemory, memoryOffset: Long)
