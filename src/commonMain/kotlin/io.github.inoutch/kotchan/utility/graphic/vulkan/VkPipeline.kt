package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkPipeline : Disposable

expect fun vkCreateGraphicsPipelines(
    device: VkDevice,
    pipelineCache: VkPipelineCache?,
    createInfos: List<VkGraphicsPipelineCreateInfo>
): VkPipeline
