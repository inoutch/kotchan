package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.shader.Descriptor
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.graphic.vulkan.*

class VKSampler(
        private val descriptorSetStacks: List<VKDescriptorSetStack>,
        private val binding: Int,
        private val otherDescriptors: List<Descriptor>) {
    // do not dispose
    private var texture: Texture? = null

    fun setTexture(vk: VK, texture: Texture) {
        val bundle = texture.vkTexture ?: throw VkInvalidStateError("VKSampler")
        if (texture != this.texture) {
            val descriptorWrites = vk.commandBuffers.mapIndexed { index, _ ->
                val descriptorSet = descriptorSetStacks[index].pop {
                    instance.graphicsApi.updateDescriptors(vk, otherDescriptors, descriptorSetStacks)
                }
                val imageInfo = VkDescriptorImageInfo(
                        bundle.sampler, bundle.imageView, VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL)
                VkWriteDescriptorSet(
                        descriptorSet, binding, 0,
                        VkDescriptorType.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER,
                        listOf(imageInfo), listOf(), listOf())
            }
            vkUpdateDescriptorSets(vk.device, descriptorWrites, listOf())
            this.texture = texture
        }
    }
}
