package io.github.inoutch.kotchan.utility

import io.github.inoutch.kotchan.extension.*
import org.lwjgl.system.NativeResource

class MemScope {
    private val natives = mutableListOf<NativeResource>()

    fun <T : NativeResource> add(native: T): T {
        natives.add(native)
        return native
    }

    fun allocPointer(size: Int = 1) = WrappedPointerBuffer.alloc(size, this).pointerBuffer

    fun allocLong(size: Int = 1) = WrappedLongBuffer.alloc(size, this).longBuffer

    fun allocInt(size: Int = 1) = WrappedIntBuffer.alloc(size, this).intBuffer

    fun allocFloat(size: Int = 1) = WrappedFloatBuffer.alloc(size, this).floatBuffer

    fun alloc(size: Int = 1) = WrappedByteBuffer.alloc(size, this).byteBuffer

    fun free(native: NativeResource) {
        native.free()
        natives.remove(native)
    }

    fun freeAll() {
        natives.forEach { it.free() }
        natives.clear()
    }
}

fun <T> memScoped(scope: MemScope.() -> T): T {
    val memoryManager = MemScope()
    try {
        val ret = scope(memoryManager)
        memoryManager.freeAll()
        return ret
    } catch (e: Error) {
        memoryManager.freeAll()
        throw e
    }
}
