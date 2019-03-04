package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkCommandPool : Disposable

expect fun vkCreateCommandPool(
        device: VkDevice,
        createInfo: VkCommandPoolCreateInfo): VkCommandPool
