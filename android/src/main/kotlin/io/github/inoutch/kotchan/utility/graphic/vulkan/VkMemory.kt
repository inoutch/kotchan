package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkDeviceMemory : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkAllocateMemory(device: VkDevice, allocateInfo: VkMemoryAllocateInfo): VkDeviceMemory {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

actual fun vkBindBufferMemory(device: VkDevice, buffer: VkBuffer, memory: VkDeviceMemory, memoryOffset: Long) {
}

actual fun vkMapMemory(device: VkDevice, memory: VkDeviceMemory, offset: Long, size: Long, flags: List<VkPipelineStageFlagBits>): MappedMemory {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
