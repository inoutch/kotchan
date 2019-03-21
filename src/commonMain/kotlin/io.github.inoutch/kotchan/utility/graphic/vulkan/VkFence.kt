package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkFence : Disposable

expect fun vkCreateFence(device: VkDevice, createInfo: VkFenceCreateInfo): VkFence

expect fun vkWaitForFences(device: VkDevice, fences: List<VkFence>, waitAll: Boolean, timeout: Long)

expect fun vkResetFences(device: VkDevice, fences: List<VkFence>)
