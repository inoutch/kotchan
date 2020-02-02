package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkSampler
import io.github.inoutch.kotlin.vulkan.api.vk

class VKSampler(
    val logicalDevice: VKLogicalDevice,
    val sampler: VkSampler
) : Disposer() {
    init {
        add { vk.destroySampler(logicalDevice.device, sampler) }
    }
}
