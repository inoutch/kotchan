package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkDescriptorSet

class VKDescriptorSet(
        val logicalDevice: VKLogicalDevice,
        val descriptorPool: VKDescriptorPool,
        val descriptorSet: VkDescriptorSet
) : Disposer()
