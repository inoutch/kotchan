package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotlin.vulkan.api.VkBufferUsageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkMemoryPropertyFlagBits
import io.github.inoutch.kotlin.vulkan.constant.FLOAT_SIZE

class VKVertexBuffer(
    logicalDevice: VKLogicalDevice,
    commandPool: VKCommandPool,
    vertices: FloatArray,
    mode: BufferStorageMode
) : VertexBuffer(mode) {
    val buffer: VKBuffer

    val bufferDeviceMemory: VKBufferDeviceMemory

    init {
        val size = vertices.size.toLong()
        val bufferTmp = logicalDevice.createBuffer(
                size * FLOAT_SIZE,
                listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT)
        )
        add(bufferTmp)

        val bufferDeviceMemoryTmp = bufferTmp.allocateBufferDeviceMemory()
        add(bufferTmp)

        val data = bufferDeviceMemoryTmp.mapMemory(0, listOf())
        data.copy(0, size, vertices)
        data.destroy()

        if (mode == BufferStorageMode.Static) {
            buffer = logicalDevice.createBuffer(
                    size,
                    listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_TRANSFER_DST_BIT,
                            VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT)
            )
            add(buffer)

            bufferDeviceMemory = buffer.allocateBufferDeviceMemory(listOf(
                    VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT
            ))
            add(bufferDeviceMemory)
            commandPool.copyBuffer(bufferTmp, 0L, buffer, 0L, size)

            dispose(bufferDeviceMemoryTmp)
            dispose(bufferTmp)
        } else {
            buffer = bufferTmp
            bufferDeviceMemory = bufferDeviceMemoryTmp
        }
    }

    override fun copyToBuffer(vertices: FloatArray, offset: Int, size: Int) {
        if (mode == BufferStorageMode.Static) {
            return
        }

        val data = bufferDeviceMemory.mapMemory(0, listOf())
        data.copy(offset.toLong(), size.toLong(), vertices)
        data.destroy()
    }
}
