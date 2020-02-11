package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.extension.getProperties
import io.github.inoutch.kotlin.vulkan.api.VkDescriptorPool
import io.github.inoutch.kotlin.vulkan.api.VkDescriptorSet
import io.github.inoutch.kotlin.vulkan.api.VkDescriptorSetAllocateInfo
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.vk

class VKDescriptorPool(
        val logicalDevice: VKLogicalDevice,
        val descriptorPool: VkDescriptorPool
) : Disposer() {
    fun allocateDescriptorSets(
            descriptorSetLayouts: List<VKDescriptorSetLayout>
    ): List<VKDescriptorSet> {
        val allocateInfo = VkDescriptorSetAllocateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO,
                descriptorPool,
                descriptorSetLayouts.size,
                descriptorSetLayouts.map { it.descriptorSetLayout }
        )
        val descriptorSets = getProperties<VkDescriptorSet> {
            vk.allocateDescriptorSets(logicalDevice.device, allocateInfo, it).value
        }
        return descriptorSets.map {
            val descriptorSet = VKDescriptorSet(logicalDevice, this, it)
            add(descriptorSet)
            descriptorSet.add { vk.freeDescriptorSets(logicalDevice.device, descriptorPool, listOf(it)) }
            descriptorSet
        }
    }
}
