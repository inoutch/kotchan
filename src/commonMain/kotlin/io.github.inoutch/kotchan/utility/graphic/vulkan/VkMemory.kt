package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkMemory : Disposable

expect fun vkAllocateMemory(device: VkDevice, allocateInfo: VkMemoryAllocateInfo): VkMemory

expect fun vkBindBufferMemory(device: VkDevice, buffer: VkBuffer, memory: VkMemory, memoryOffset: Long)

expect fun vkMapMemory(device: VkDevice, memory: VkMemory, offset: Long, size: Long, flags: List<VkPipelineStageFlagBits>): MappedMemory
