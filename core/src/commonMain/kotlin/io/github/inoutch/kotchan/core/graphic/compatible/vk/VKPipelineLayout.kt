package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkPipelineLayout
import io.github.inoutch.kotlin.vulkan.api.vk

class VKPipelineLayout(
        val logicalDevice: VKLogicalDevice,
        val pipelineLayout: VkPipelineLayout
) : Disposer() {
    init {
        add { vk.destroyPipelineLayout(logicalDevice.device, pipelineLayout) }
    }
}
