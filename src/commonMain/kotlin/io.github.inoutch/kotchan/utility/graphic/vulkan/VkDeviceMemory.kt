package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkDeviceMemory : Disposable

expect fun vkAllocateMemory(device: VkDevice, allocateInfo: VkMemoryAllocateInfo): VkDeviceMemory

expect fun vkBindBufferMemory(device: VkDevice, buffer: VkBuffer, memory: VkDeviceMemory, memoryOffset: Long)

expect fun vkMapMemory(device: VkDevice, memory: VkDeviceMemory, offset: Long, size: Long, flags: List<VkPipelineStageFlagBits>): MappedMemory
