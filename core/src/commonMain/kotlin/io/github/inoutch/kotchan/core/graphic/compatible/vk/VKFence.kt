package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkFence

class VKFence(val logicalDevice: VKLogicalDevice, val fence: VkFence) : Disposer()
