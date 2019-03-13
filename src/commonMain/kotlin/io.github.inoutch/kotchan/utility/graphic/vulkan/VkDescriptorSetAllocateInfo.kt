package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkDescriptorSetAllocateInfo(
        val descriptorPool: VkDescriptorPool,
        val descriptorSetCount: Int,
        val setLayouts: List<VkDescriptorSetLayout>)
