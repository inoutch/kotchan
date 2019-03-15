package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.graphic.vulkan.*

class VKSampler(private val descriptorSets: List<VkDescriptorSet>, private val binding: Int) {
    // do not dispose
    private var texture: Texture? = null

    fun setTexture(vk: VK, texture: Texture) {
        val bundle = texture.vkTexture ?: throw VkInvalidStateError("VKSampler")
        if (texture != this.texture) {
            val imageInfo = VkDescriptorImageInfo(
                    bundle.sampler, bundle.imageView, VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL)
            val descriptorWrite = VkWriteDescriptorSet(
                    descriptorSets[vk.currentImageIndex],
                    binding,
                    0,
                    VkDescriptorType.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER,
                    listOf(imageInfo), listOf(), listOf())
            vkUpdateDescriptorSets(vk.device, listOf(descriptorWrite), listOf())
            this.texture = texture
        }
    }
}
