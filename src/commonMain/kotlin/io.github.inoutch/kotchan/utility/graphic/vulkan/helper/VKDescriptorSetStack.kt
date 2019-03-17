package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.graphic.vulkan.VK
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkDescriptorPool
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkDescriptorSet
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkDescriptorSetLayout

class VKDescriptorSetStack(private val vk: VK,
                           private val descriptorPool: VkDescriptorPool,
                           private val layout: VkDescriptorSetLayout) {
    private val descriptorSets = mutableListOf<VkDescriptorSet>()

    private var current = 0

    val all: List<VkDescriptorSet>
        get() = descriptorSets

    init {
        descriptorSets.addAll(vk.createDescriptorSets(vk.device, descriptorPool, 1, layout))
    }

    fun get() = descriptorSets[current - 1]

    fun pop(createdCallback: ((descriptorSet: VkDescriptorSet) -> Unit)?): VkDescriptorSet {
        if (descriptorSets.size <= current) {
            val newDescriptorSet = vk.createDescriptorSets(vk.device, descriptorPool, 1, layout).first()
            createdCallback?.invoke(newDescriptorSet)
            descriptorSets.add(newDescriptorSet)
        }
        return descriptorSets[current++]
    }

    fun clear() {
        current = 0
    }
}
