package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkPipeline
import io.github.inoutch.kotlin.vulkan.api.vk

class VKPipeline(
        val logicalDevice: VKLogicalDevice,
        val pipeline: VkPipeline
) : Disposer() {
    init {
        add { vk.destroyPipeline(logicalDevice.device, pipeline) }
    }
}
