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
        array.usePinned {
            val p = (native.rawValue.toLong() + offset * FLOAT_SIZE).toCPointer<FloatVar>()
            memcpy(p, it.addressOf(0), (size * FLOAT_SIZE).toULong())
        }
        Unit
    }

    @ExperimentalUnsignedTypes
    actual fun copy(offset: Long, size: Long, array: IntArray) = memScoped {
        array.usePinned {
            val p = (native.rawValue.toLong() + offset * INT_SIZE).toCPointer<IntVar>()
            memcpy(p, it.addressOf(0), (size * INT_SIZE).toULong())
        }
        Unit
    }

    @ExperimentalUnsignedTypes
    actual fun copy(offset: Long, size: Long, array: ByteArray) = memScoped {
        array.usePinned {
            val p = (native.rawValue.toLong() + offset).toCPointer<ByteVar>()
            memcpy(p, it.addressOf(0), size.toULong())
        }
        Unit
    }
}
