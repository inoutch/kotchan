package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkDescriptorSet : Disposable

expect fun vkAllocateDescriptorSets(device: VkDevice, allocateInfo: VkDescriptorSetAllocateInfo): List<VkDescriptorSet>

expect fun vkUpdateDescriptorSets(
        device: VkDevice,
        descriptorWrites: List<VkWriteDescriptorSet>,
        descriptorCopies: List<VkCopyDescriptorSet>)
