package io.github.inoutch.kotchan.extension

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.PointerBuffer
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.NativeResource
import org.lwjgl.system.Pointer
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer

fun List<String>.stringsToNative(scope: MemScope): PointerBuffer {
    val utf8Strings = this.map { MemoryUtil.memUTF8(it) }
    val native = scope.allocPointer(utf8Strings.size)
    utf8Strings.forEach { native.put(it) }
    native.flip()
    return native
}

fun <T : Pointer> List<T>.pointerBuffersToNative(scope: MemScope): PointerBuffer {
    val native = scope.allocPointer(size)
    forEach { native.put(it) }
    native.flip()
    return native
}

class WrappedPointerBuffer private constructor(val pointerBuffer: PointerBuffer) : NativeResource {
    companion object {
        fun alloc(size: Int) = WrappedPointerBuffer(MemoryUtil.memAllocPointer(size))
        fun alloc(size: Int, memScope: MemScope) = memScope.add(alloc(size))
    }

    override fun free() {
        MemoryUtil.memFree(pointerBuffer)
    }
}

class WrappedLongBuffer private constructor(val longBuffer: LongBuffer) : NativeResource {
    companion object {
        fun alloc(size: Int) = WrappedLongBuffer(MemoryUtil.memAllocLong(size))
        fun alloc(size: Int, memScope: MemScope) = memScope.add(alloc(size))
    }

    override fun free() {
        MemoryUtil.memFree(longBuffer)
    }
}

class WrappedIntBuffer private constructor(val intBuffer: IntBuffer) : NativeResource {
    companion object {
        fun alloc(size: Int) = WrappedIntBuffer(MemoryUtil.memAllocInt(size))
        fun alloc(size: Int, memScope: MemScope) = memScope.add(alloc(size))
    }

    override fun free() {
        MemoryUtil.memFree(intBuffer)
    }
}

class WrappedFloatBuffer private constructor(val floatBuffer: FloatBuffer) : NativeResource {
    companion object {
        fun alloc(size: Int) = WrappedFloatBuffer(MemoryUtil.memAllocFloat(size))
        fun alloc(size: Int, memScope: MemScope) = memScope.add(alloc(size))
    }

    override fun free() {
        MemoryUtil.memFree(floatBuffer)
    }
}

class WrappedByteBuffer private constructor(val byteBuffer: ByteBuffer) : NativeResource {
    companion object {
        fun alloc(size: Int) = WrappedByteBuffer(MemoryUtil.memAlloc(size))
        fun alloc(size: Int, memScope: MemScope) = memScope.add(alloc(size))
    }

    override fun free() {
        MemoryUtil.memFree(byteBuffer)
    }
}


fun PointerBuffer.toStringList(): List<String> {
    val size = remaining()
    return List(size) { getStringUTF8(it) }
}

fun IntArray.toNative(scope: MemScope): IntBuffer {
    val ret = scope.allocInt(size)
    forEach { ret.put(it) }
    ret.flip()
    return ret
}

fun LongArray.toNative(scope: MemScope): LongBuffer {
    val ret = scope.allocLong(size)
    forEach { ret.put(it) }
    ret.flip()
    return ret
}

fun FloatArray.toNative(scope: MemScope): FloatBuffer {
    val ret = scope.allocFloat(size)
    forEach { ret.put(it) }
    ret.flip()
    return ret
}

fun ByteArray.toNative(scope: MemScope): ByteBuffer {
    val ret = scope.alloc(size)
    forEach { ret.put(it) }
    ret.flip()
    return ret
}
