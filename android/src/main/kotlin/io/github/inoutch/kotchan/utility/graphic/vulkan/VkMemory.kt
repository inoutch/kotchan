package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkMemory : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkAllocateMemory(device: VkDevice, allocateInfo: VkMemoryAllocateInfo): VkMemory {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

actual fun vkBindBufferMemory(device: VkDevice, buffer: VkBuffer, memory: VkMemory, memoryOffset: Long) {
}

actual fun vkMapMemory(device: VkDevice, memory: VkMemory, offset: Long, size: Long, flags: List<VkPipelineStageFlagBits>): MappedMemory {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
