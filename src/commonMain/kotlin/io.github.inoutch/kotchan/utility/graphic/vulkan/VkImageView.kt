package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkImageView : Disposable

expect fun vkCreateImageView(device: VkDevice, createInfo: VkImageViewCreateInfo): VkImageView
