package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkDeviceMemory : Disposable {
    lateinit var native: vulkan.VkDeviceMemory
        private set

    private lateinit var device: VkDevice

    fun init(nativeDeviceMemory: vulkan.VkDeviceMemory, device: VkDevice) {
        this.native = nativeDeviceMemory
        this.device = device
    }

    override fun dispose() {
        vulkan.vkFreeMemory(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkAllocateMemory(device: VkDevice, allocateInfo: VkMemoryAllocateInfo) = memScoped {
    val native = alloc<vulkan.VkDeviceMemoryVar>()

    checkError(vulkan.vkAllocateMemory(device.native, allocateInfo.toNative(this), null, native.ptr))

    VkDeviceMemory().apply { init(native.value ?: throw VkNullError("deviceMemory"), device) }
}

@ExperimentalUnsignedTypes
actual fun vkBindBufferMemory(device: VkDevice, buffer: VkBuffer, memory: VkDeviceMemory, memoryOffset: Long) {
    checkError(vulkan.vkBindBufferMemory(device.native, buffer.native, memory.native, memoryOffset.toULong()))
}

@ExperimentalUnsignedTypes
actual fun vkMapMemory(device: VkDevice, memory: VkDeviceMemory, offset: Long, size: Long, flags: List<VkPipelineStageFlagBits>) = memScoped {
    val native = alloc<COpaquePointerVar>()

    checkError(vulkan.vkMapMemory(
            device.native,
            memory.native,
            offset.toULong(),
            size.toULong(),
            flags.sumBy { it.value }.toUInt(),
            native.ptr))

    MappedMemory(size).apply { init(native.value ?: throw VkNullError("mappedMemory"), device, memory) }
}
