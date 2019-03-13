package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkBufferView : Disposable

expect fun vkCreateBufferView(device: VkDevice, createInfo: VkBufferViewCreateInfo): VkBufferView
