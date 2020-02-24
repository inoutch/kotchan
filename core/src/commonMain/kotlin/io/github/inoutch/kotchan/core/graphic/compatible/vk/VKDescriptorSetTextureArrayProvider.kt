package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotlin.vulkan.api.VkDescriptorImageInfo
import io.github.inoutch.kotlin.vulkan.api.VkDescriptorType
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout
import io.github.inoutch.kotlin.vulkan.api.VkWriteDescriptorSet

class VKDescriptorSetTextureArrayProvider(private val descriptorSet: VKDescriptorSet) {
    private class TextureBundle(val binding: Int, val textures: List<VKTexture>)

    private var bundle: TextureBundle? = null

    var isChanged = true
        private set

    fun set(binding: Int, textures: List<VKTexture>) {
        if (textures != this.bundle?.textures) {
            this.bundle = TextureBundle(binding, textures)
            isChanged = true
        }
    }

    fun writeDescriptorSet(): VkWriteDescriptorSet? {
        if (!isChanged) {
            return null
        }
        val bundle = this.bundle ?: return null
        val imageInfos = bundle.textures.map {
            VkDescriptorImageInfo(
                    it.sampler.sampler,
                    it.imageView.imageView,
                    VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL
            )
        }
        isChanged = false
        return VkWriteDescriptorSet(
                descriptorSet.descriptorSet,
                bundle.binding,
                0,
                VkDescriptorType.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER,
                imageInfos,
                listOf(),
                listOf()
        )
    }
}
