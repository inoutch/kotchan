package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.*

class VKPipeline(
        val vk: VK,
        val pipeline: VkPipeline,
        val descriptorSetLayout: VkDescriptorSetLayout,
        val descriptorSetProvider: DescriptorSetProvider,
        val pipelineLayout: VkPipelineLayout) : Disposable {
    override fun dispose() {
        vkQueueWaitIdle(vk.queue)
        descriptorSetLayout.dispose()
        pipelineLayout.dispose()
        pipeline.dispose()
    }
}
