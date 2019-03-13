package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkDescriptorPool : Disposable

expect fun vkCreateDescriptorPool(device: VkDevice, createInfo: VkDescriptorPoolCreateInfo): VkDescriptorPool
