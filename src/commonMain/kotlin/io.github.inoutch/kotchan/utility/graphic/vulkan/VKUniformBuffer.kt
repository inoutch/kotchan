package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

class VKUniformBuffer(val vk: VK, val binding: Int, size: Long) : Disposable {

    val buffers = List(vk.commandBuffers.size) {
        VKBufferMemory(
                vk, size, listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_UNIFORM_BUFFER_BIT),
                listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT))
    }

    fun copy(array: FloatArray, offset: Long) {
        val data = vkMapMemory(vk.device, buffers[vk.currentImageIndex].memory, offset, array.size * FLOAT_SIZE, listOf())
        data.copy(0, array.size * FLOAT_SIZE, array)
        data.dispose()
    }

    fun copy(array: IntArray, offset: Long) {
        val data = vkMapMemory(vk.device, buffers[vk.currentImageIndex].memory, offset, array.size * INT_SIZE, listOf())
        data.copy(0, array.size * INT_SIZE, array)
        data.dispose()
    }

    fun copy(array: ByteArray, offset: Long) {
        val data = vkMapMemory(vk.device, buffers[vk.currentImageIndex].memory, offset, array.size.toLong(), listOf())
        data.copy(0, array.size.toLong(), array)
        data.dispose()
    }

    override fun dispose() {
        buffers.forEach { it.dispose() }
    }
}
