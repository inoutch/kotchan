package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkBufferUsageFlagBits

class VKUniformBuffer(
    val logicalDevice: VKLogicalDevice,
    val size: Int
) : Disposer() {
    val buffer: VKBuffer

    val bufferDeviceMemory: VKBufferDeviceMemory

    init {
        buffer = logicalDevice.createBuffer(
                size.toLong(),
                listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_UNIFORM_BUFFER_BIT)
        )
        add(buffer)

        bufferDeviceMemory = buffer.allocateBufferDeviceMemory()
        add(bufferDeviceMemory)
    }

    fun copyToBuffer(array: FloatArray, offset: Int) {
        val data = bufferDeviceMemory.mapMemory(offset.toLong(), listOf())
        data.copy(0, size.toLong(), array)
        data.destroy()
    }

    fun copyToBuffer(array: IntArray, offset: Int) {
        val data = bufferDeviceMemory.mapMemory(offset.toLong(), listOf())
        data.copy(0, size.toLong(), array)
        data.destroy()
    }
}
