package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import kotlin.test.Test
import kotlin.test.assertEquals

class BatchBufferTest {
    @Test
    fun checkAllocate() {
        val nativeBuffer = FloatArray(1000) { 0.0f }
        val batchBuffer = BatchBuffer(1000) {
            object : VertexBuffer(BufferStorageMode.Dynamic) {
                override fun copyToBuffer(vertices: FloatArray, offset: Int, size: Int) {
                    vertices.copyOfRange(offset, offset + size).copyInto(nativeBuffer, offset)
                }
            }
        }
        assertEquals(1000, batchBuffer.vertices.size)
        assertEquals(0, batchBuffer.size)

        val pointer1 = batchBuffer.allocate(30)
        assertEquals(30, batchBuffer.size)
        batchBuffer.range(pointer1.first, pointer1.first + 30)
        for (i in 0 until 30) {
            batchBuffer.copy(pointer1.first + i, i.toFloat())
        }
        for (i in 0 until 30) {
            assertEquals(.0f, nativeBuffer[i])
        }
        batchBuffer.flush()
        for (i in 0 until 30) {
            assertEquals(i.toFloat(), nativeBuffer[i])
        }

        val pointer2 = batchBuffer.allocate(10)
        assertEquals(40, batchBuffer.size)
        copy(batchBuffer, pointer2, 30)
        batchBuffer.flush()

        for (i in 0 until 40) {
            assertEquals(i.toFloat(), nativeBuffer[i])
        }
    }

    @Test
    fun checkRealloc() {
        val nativeBuffer = FloatArray(1000) { 0.0f }
        val batchBuffer = BatchBuffer(1000) {
            object : VertexBuffer(BufferStorageMode.Dynamic) {
                override fun copyToBuffer(vertices: FloatArray, offset: Int, size: Int) {
                    vertices.copyOfRange(offset, offset + size).copyInto(nativeBuffer, offset)
                }
            }
        }
        val pointer1 = batchBuffer.allocate(10)
        val pointer2 = batchBuffer.allocate(15)
        copy(batchBuffer, pointer1, 0)
        copy(batchBuffer, pointer2, pointer1.last())
        batchBuffer.flush()

        for (i in 0 until 25) {
            assertEquals(i.toFloat(), nativeBuffer[i])
        }
        batchBuffer.reallocate(pointer1, 20)
        assertEquals(35, batchBuffer.size)
        copy(batchBuffer, pointer1, 0)
        copy(batchBuffer, pointer2, pointer1.last())

        batchBuffer.flush()
        assertEquals(20, pointer1.size)
        assertEquals(0, pointer1.first)
        assertEquals(15, pointer2.size)
        assertEquals(20, pointer2.first)
        for (i in 0 until 35) {
            assertEquals(i.toFloat(), nativeBuffer[i])
        }
    }

    @Test
    fun checkFree() {
        val nativeBuffer = FloatArray(1000) { 0.0f }
        val batchBuffer = BatchBuffer(1000) {
            object : VertexBuffer(BufferStorageMode.Dynamic) {
                override fun copyToBuffer(vertices: FloatArray, offset: Int, size: Int) {
                    vertices.copyOfRange(offset, offset + size).copyInto(nativeBuffer, offset)
                }
            }
        }

        val pointer1 = batchBuffer.allocate(10)
        val pointer2 = batchBuffer.allocate(15)
        batchBuffer.free(pointer1)
        assertEquals(0, pointer2.first)
        assertEquals(15, batchBuffer.size)
    }

    @Test
    fun checkReallocateVertexBuffer() {
        val nativeBuffer = FloatArray(100) { 0.0f }
        val batchBuffer = BatchBuffer(5) {
            object : VertexBuffer(BufferStorageMode.Dynamic) {
                override fun copyToBuffer(vertices: FloatArray, offset: Int, size: Int) {
                    vertices.copyOfRange(offset, offset + size).copyInto(nativeBuffer, offset)
                }
            }
        }
        batchBuffer.allocate(5)
        assertEquals(5, batchBuffer.vertices.size)

        batchBuffer.allocate(5)
        assertEquals(20, batchBuffer.vertices.size)

        assertEquals(10, batchBuffer.size)
    }

    @Test
    fun checkPartialChange() {
        val nativeBuffer = FloatArray(100) { 0.0f }
        val batchBuffer = BatchBuffer(5) {
            object : VertexBuffer(BufferStorageMode.Dynamic) {
                override fun copyToBuffer(vertices: FloatArray, offset: Int, size: Int) {
                    vertices.copyOfRange(offset, offset + size).copyInto(nativeBuffer, offset)
                }
            }
        }
        batchBuffer.allocate(10)
        val pointer = batchBuffer.allocate(5)
        batchBuffer.range(pointer.first + 2, pointer.first + 4)
        batchBuffer.copy(pointer.first + 2, -1.0f)
        batchBuffer.copy(pointer.first + 3, -1.0f)
        batchBuffer.flush()

        for (i in 0 until 15) {
            assertEquals(if (i == 12 || i == 13) -1.0f else 0.0f, nativeBuffer[i])
        }
    }

    private fun copy(batchBuffer: BatchBuffer, pointer: BatchBufferPointer, firstValue: Int) {
        batchBuffer.range(pointer.first, pointer.last())
        for (i in 0 until pointer.size) {
            batchBuffer.copy(pointer.first + i, (firstValue + i).toFloat())
        }
    }
}