package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*
import platform.posix.memcpy

actual class MappedMemory actual constructor(size: Long) : Disposable {
    lateinit var native: COpaquePointer

    private lateinit var device: VkDevice

    private lateinit var memory: VkDeviceMemory

    fun init(nativeMappedMemory: COpaquePointer, device: VkDevice, memory: VkDeviceMemory) {
        this.native = nativeMappedMemory
        this.device = device
        this.memory = memory
    }

    override fun dispose() {
        vulkan.vkUnmapMemory(device.native, memory.native)
    }

    actual val bytesSize = size

    @ExperimentalUnsignedTypes
    actual fun copy(offset: Long, size: Long, array: FloatArray) = memScoped {
        val p = array.refTo(0).getPointer(this).toLong() + offset * FLOAT_SIZE
        memcpy(native, p.toCPointer<ByteVar>(), (size * FLOAT_SIZE).toULong())
        List(size.toInt()) { print(native.rawValue.toLong().toCPointer<FloatVar>()?.get(it)); print(",") }
        println()
        Unit
    }

    @ExperimentalUnsignedTypes
    actual fun copy(offset: Long, size: Long, array: IntArray) = memScoped {
        val p = array.refTo(0).getPointer(this).toLong() + offset * INT_SIZE
        memcpy(native, p.toCPointer<ByteVar>(), (size * INT_SIZE).toULong())
        Unit
    }
}
