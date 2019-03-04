package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkFramebuffer : Disposable

expect fun vkCreateFramebuffer(device: VkDevice, createInfo: VkFramebufferCreateInfo): VkFramebuffer
