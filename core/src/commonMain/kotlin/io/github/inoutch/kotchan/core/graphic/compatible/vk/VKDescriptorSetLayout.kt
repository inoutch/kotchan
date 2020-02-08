package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkDescriptorSetLayout
import io.github.inoutch.kotlin.vulkan.api.vk

class VKDescriptorSetLayout(
        val logicalDevice: VKLogicalDevice,
        val descriptorSetLayout: VkDescriptorSetLayout
) : Disposer() {
    init {
        add { vk.destroyDescriptorSetLayout(logicalDevice.device, descriptorSetLayout) }
    }
}
