package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkPipelineCache : Disposable

expect fun vkCreatePipelineCache(device: VkDevice, pipelineCacheCreateInfo: VkPipelineCacheCreateInfo): VkPipelineCache
