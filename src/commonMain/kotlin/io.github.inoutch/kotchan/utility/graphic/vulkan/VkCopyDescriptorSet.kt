package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkCopyDescriptorSet(
        val srcSet: VkDescriptorSet,
        val srcBinding: Int,
        val srcArrayElement: Int,
        val dstSet: VkDescriptorSet,
        val dstBinding: Int,
        val dstArrayElement: Int,
        val descriptorCount: Int)
