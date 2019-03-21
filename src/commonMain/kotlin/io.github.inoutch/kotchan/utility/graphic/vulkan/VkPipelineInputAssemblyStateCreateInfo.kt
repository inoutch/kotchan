package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineInputAssemblyStateCreateInfo(
        val flags: Int,
        val topology: VkPrimitiveTopology,
        val primitiveRestartEnable: Boolean)
