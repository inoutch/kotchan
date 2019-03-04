package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkPipelineLayout : Disposable

expect fun vkCreatePipelineLayout(device: VkDevice, createInfo: VkPipelineLayoutCreateInfo): VkPipelineLayout
