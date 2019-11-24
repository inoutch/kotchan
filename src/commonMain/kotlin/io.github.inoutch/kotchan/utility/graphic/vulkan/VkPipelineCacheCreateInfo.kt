package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineCacheCreateInfo(
    val flags: Int,
    val initialDataSize: Long,
    val initialData: ByteArray
)
