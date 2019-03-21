package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkSemaphore : Disposable

expect fun vkCreateSemaphore(device: VkDevice, createInfo: VkSemaphoreCreateInfo): VkSemaphore
