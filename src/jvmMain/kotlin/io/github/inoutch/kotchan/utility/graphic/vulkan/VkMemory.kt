package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkMemory : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    fun init(nativeMemory: Long, device: VkDevice) {
        this.native = nativeMemory
        this.device = device
    }

    override fun dispose() {
        VK10.vkFreeMemory(device.native, native, null)
    }
}

actual fun vkAllocateMemory(device: VkDevice, allocateInfo: VkMemoryAllocateInfo) = memScoped {
    val native = allocLong()

    checkError(VK10.vkAllocateMemory(device.native, allocateInfo.toNative(this), null, native))

    VkMemory().apply { init(native.get(0), device) }
}

actual fun vkBindBufferMemory(device: VkDevice, buffer: VkBuffer, memory: VkMemory, memoryOffset: Long) {
    checkError(VK10.vkBindBufferMemory(device.native, buffer.native, memory.native, memoryOffset))
}

actual fun vkMapMemory(device: VkDevice, memory: VkMemory, offset: Long, size: Long, flags: List<VkPipelineStageFlagBits>) = memScoped {
    val native = allocPointer()
    checkError(VK10.vkMapMemory(device.native, memory.native, offset, size, flags.sumBy { it.value }, native))

    MappedMemory(size).apply { init(native.get(0), device, memory) }
}
