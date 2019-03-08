package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.system.MemoryUtil
import org.lwjgl.vulkan.VK10

actual class MappedMemory actual constructor(size: Long) : Disposable {

    actual val bytesSize = size

    var native: Long = 0

    private lateinit var device: VkDevice

    private lateinit var memory: VkDeviceMemory

    fun init(native: Long, device: VkDevice, memory: VkDeviceMemory) {
        this.native = native
        this.device = device
        this.memory = memory
    }

    override fun dispose() {
        VK10.vkUnmapMemory(device.native, memory.native)
    }

    actual fun copy(offset: Long, size: Long, array: FloatArray) = memScoped {
        val buffer = array.toNative(this)
        MemoryUtil.memCopy(MemoryUtil.memAddress(buffer) + offset * FLOAT_SIZE, native, size * FLOAT_SIZE)
    }

    actual fun copy(offset: Long, size: Long, array: IntArray) = memScoped {
        val buffer = array.toNative(this)
        MemoryUtil.memCopy(MemoryUtil.memAddress(buffer) + offset * INT_SIZE, native, size * INT_SIZE)
    }
}
