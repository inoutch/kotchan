package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotlin.vulkan.api.VkDescriptorImageInfo
import io.github.inoutch.kotlin.vulkan.api.VkDescriptorType
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout
import io.github.inoutch.kotlin.vulkan.api.VkWriteDescriptorSet

class VKDescriptorSetTextureProvider(private val descriptorSet: VKDescriptorSet) {
    private class TextureBundle(val binding: Int, val texture: VKTexture)

    private var bundle: TextureBundle? = null

    var isChanged = true
        private set

    fun set(binding: Int, texture: VKTexture) {
        if (texture != this.bundle?.texture) {
            this.bundle = TextureBundle(binding, texture)
            isChanged = true
        }
    }

    fun writeDescriptorSet(): VkWriteDescriptorSet? {
        val bundle = this.bundle ?: return null
        val imageInfo = VkDescriptorImageInfo(
                bundle.texture.sampler.sampler,
                bundle.texture.imageView.imageView,
                VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL
        )
        isChanged = false
        return VkWriteDescriptorSet(
                descriptorSet.descriptorSet,
                bundle.binding,
                0,
                VkDescriptorType.VK_DESCRIPTOR_TYPE_SAMPLER,
                listOf(imageInfo),
                listOf(),
                listOf()
        )
    }
}
