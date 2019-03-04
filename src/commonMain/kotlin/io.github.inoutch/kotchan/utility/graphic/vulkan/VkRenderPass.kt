package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkRenderPass : Disposable

expect fun vkCreateRenderPass(device: VkDevice, createInfo: VkRenderPassCreateInfo): VkRenderPass
