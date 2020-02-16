package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotlin.vulkan.api.VkDescriptorBufferInfo
import io.github.inoutch.kotlin.vulkan.api.VkDescriptorType
import io.github.inoutch.kotlin.vulkan.api.VkWriteDescriptorSet

class VKDescriptorSetUniformProvider(private val descriptorSet: VKDescriptorSet) {
    private class UniformBundle(val binding: Int, val uniform: VKUniformBuffer)

    private var bundle: UniformBundle? = null

    var isChanged = true
        private set

    fun set(binding: Int, uniform: VKUniformBuffer) {
        if (uniform != this.bundle?.uniform) {
            this.bundle = UniformBundle(binding, uniform)
            isChanged = true
        }
    }

    fun writeDescriptorSet(): VkWriteDescriptorSet? {
        if (!isChanged) {
            return null
        }
        val bundle = this.bundle ?: return null
        val bufferInfo = VkDescriptorBufferInfo(
                bundle.uniform.buffer.buffer,
                0,
                bundle.uniform.size.toLong()
        )
        isChanged = false
        return VkWriteDescriptorSet(
                descriptorSet.descriptorSet,
                bundle.binding,
                0,
                VkDescriptorType.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER,
                listOf(),
                listOf(bufferInfo),
                listOf()
        )
    }
}
