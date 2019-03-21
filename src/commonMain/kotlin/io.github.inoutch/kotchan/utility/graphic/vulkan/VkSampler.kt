package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkSampler : Disposable

expect fun vkCreateSampler(device: VkDevice, createInfo: VkSamplerCreateInfo): VkSampler
