package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkDescriptorSetLayout : Disposable

expect fun vkCreateDescriptorSetLayout(
    device: VkDevice,
    createInfo: VkDescriptorSetLayoutCreateInfo
): VkDescriptorSetLayout
