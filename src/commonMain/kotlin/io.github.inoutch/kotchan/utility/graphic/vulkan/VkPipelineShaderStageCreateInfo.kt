package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineShaderStageCreateInfo(
        val flags: Int,
        val stage: List<VkShaderStageFlagBits>,
        val module: VkShaderModule,
        val name: String,
        val specializationInfo: VkSpecializationInfo?)
