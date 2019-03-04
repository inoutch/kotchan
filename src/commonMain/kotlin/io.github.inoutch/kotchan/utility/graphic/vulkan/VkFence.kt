package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkFence : Disposable

expect fun vkCreateFence(device: VkDevice, createInfo: VkFenceCreateInfo): VkFence
