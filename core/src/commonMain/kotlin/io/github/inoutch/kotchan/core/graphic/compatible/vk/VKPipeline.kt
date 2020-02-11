package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkPipeline

class VKPipeline(
    val logicalDevice: VKLogicalDevice,
    val pipeline: VkPipeline
) : Disposer()
