package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkWriteDescriptorSet(
    val dstSet: VkDescriptorSet,
    val dstBinding: Int,
    val dstArrayElement: Int,
    val descriptorType: VkDescriptorType,
    val imageInfo: List<VkDescriptorImageInfo>,
    val bufferInfo: List<VkDescriptorBufferInfo>,
    val texelBufferView: List<VkBufferView>
)
