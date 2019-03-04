package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkBuffer : Disposable

expect fun vkCreateBuffer(device: VkDevice, createInfo: VkBufferCreateInfo): VkBuffer

