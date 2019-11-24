package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.*

class VKPipeline(
    val vk: VK,
    val pipeline: VkPipeline,
    val descriptorSetLayout: VkDescriptorSetLayout,
    val descriptorSetProvider: DescriptorSetProvider,
    val pipelineLayout: VkPipelineLayout,
    val uniforms: List<VKUniformBuffer>,
    val samplers: List<VKSampler>
) : Disposable {

    override fun dispose() {
        vk.waitQueue {
            descriptorSetProvider.dispose()
            uniforms.forEach { it.dispose() }
//        samplers.forEach { it.dipose() }
            descriptorSetLayout.dispose()
            pipelineLayout.dispose()
            pipeline.dispose()
        }
    }
}
