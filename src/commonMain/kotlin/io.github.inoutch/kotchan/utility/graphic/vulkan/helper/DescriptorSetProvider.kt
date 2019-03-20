package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.graphic.vulkan.*

class DescriptorSetProvider(private val vk: VK,
                            private val layout: VkDescriptorSetLayout,
                            private val uniforms: List<VKUniformBuffer>,
                            private val samplerSize: Int) {
    private data class Bundle(val textures: List<VKTexture>, val descriptorSets: List<VkDescriptorSet>)

    private val bundles = mutableListOf<Bundle>()

    // binding, texture
    private val bindingTextures = mutableMapOf<Int, VKTexture>()

    private val descriptorPool = vkCreateDescriptorPool(vk.device,
            VkDescriptorPoolCreateInfo(listOf(), 64, listOf(
                    VkDescriptorPoolSize(VkDescriptorType.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER, 32),
                    VkDescriptorPoolSize(VkDescriptorType.VK_DESCRIPTOR_TYPE_SAMPLED_IMAGE, 32))))

    fun updateSampler(binding: Int, texture: VKTexture) {
        bindingTextures[binding] = texture
    }

    val currentDescriptorSet: VkDescriptorSet
        get() {
            if (bindingTextures.values.size != samplerSize) {
                throw Error("must bind textures [current bindings = ${bindingTextures.values.map { it.image }}]")
            }
            val exists = bundles.find { bindingTextures.values.toList().isEqualAll(it.textures) }
            return if (exists == null) {
                val descriptorSetCount = vk.commandBuffers.size
                val allocateInfo = VkDescriptorSetAllocateInfo(descriptorPool, descriptorSetCount, listOf(layout))
                val descriptorSets = vkAllocateDescriptorSets(vk.device, allocateInfo)
                val writeDescriptorSets = mutableListOf<VkWriteDescriptorSet>()

                writeDescriptorSets.addAll(uniforms.map {
                    val uniform = it.buffers.first()
                    val bufferInfo = VkDescriptorBufferInfo(uniform.buffer, 0, uniform.size)
                    descriptorSets.map { descriptorSet ->
                        VkWriteDescriptorSet(descriptorSet, it.binding, 0,
                                VkDescriptorType.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER,
                                listOf(), listOf(bufferInfo), listOf())
                    }
                }.flatten())

                writeDescriptorSets.addAll(bindingTextures.map {
                    val imageInfo = VkDescriptorImageInfo(
                            it.value.sampler,
                            it.value.imageView,
                            VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL)

                    descriptorSets.map { descriptorSet ->
                        VkWriteDescriptorSet(
                                descriptorSet, it.key, 0,
                                VkDescriptorType.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER,
                                listOf(imageInfo), listOf(), listOf())
                    }
                }.flatten())
                vkUpdateDescriptorSets(vk.device, writeDescriptorSets, listOf())

                bundles.add(Bundle(bindingTextures.values.toList(), descriptorSets))
                descriptorSets
            } else {
                exists.descriptorSets
            }[vk.currentImageIndex]
        }
}

fun <T> List<T>.isEqualAll(list: List<T>): Boolean {
    if (size != list.size) {
        return false
    }
    for (i in 0 until this.size) {
        if (this[i] != list[i]) {
            return false
        }
    }
    return true
}
