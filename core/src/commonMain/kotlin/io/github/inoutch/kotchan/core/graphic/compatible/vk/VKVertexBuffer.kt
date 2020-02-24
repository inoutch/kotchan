package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotlin.vulkan.api.VkBufferUsageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkMemoryPropertyFlagBits
import io.github.inoutch.kotlin.vulkan.constant.FLOAT_SIZE
import kotlin.math.max

class VKVertexBuffer(
        logicalDevice: VKLogicalDevice,
        commandPool: VKCommandPool,
        intVertices: IntArray = IntArray(0),
        floatVertices: FloatArray = FloatArray(0),
        mode: BufferStorageMode
) : VertexBuffer(mode) {
    val buffer: VKBuffer

    val bufferDeviceMemory: VKBufferDeviceMemory

    init {
        val byteSize = 4 // Int == Float == 4
        val size = max(intVertices.size, floatVertices.size).toLong()
        check(size > 0) { "vertices is empty" }

        val bufferTmp = logicalDevice.createBuffer(
                size * byteSize,
                listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT)
        )
        add(bufferTmp)

        val bufferDeviceMemoryTmp = bufferTmp.allocateBufferDeviceMemory()
        add(bufferDeviceMemoryTmp)

        val data = bufferDeviceMemoryTmp.mapMemory(0, listOf())
        if (intVertices.isNotEmpty()) {
            data.copy(0, size, intVertices)
        } else {
            data.copy(0, size, floatVertices)
        }
        data.destroy()

        if (mode == BufferStorageMode.Static) {
            buffer = logicalDevice.createBuffer(
                    size * FLOAT_SIZE,
                    listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_TRANSFER_DST_BIT,
                            VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT)
            )
            add(buffer)

            bufferDeviceMemory = buffer.allocateBufferDeviceMemory(listOf(
                    VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT
            ))
            add(bufferDeviceMemory)
            commandPool.copyBuffer(bufferTmp, 0L, buffer, 0L, size * byteSize)

            dispose(bufferDeviceMemoryTmp)
            dispose(bufferTmp)
        } else {
            buffer = bufferTmp
            bufferDeviceMemory = bufferDeviceMemoryTmp
        }
    }

    override fun copyToBuffer(vertices: IntArray, offset: Int, size: Int) {
        if (mode == BufferStorageMode.Static) {
            return
        }

        val data = bufferDeviceMemory.mapMemory(0, listOf())
        data.copy(offset.toLong(), size.toLong(), vertices)
        data.destroy()
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
