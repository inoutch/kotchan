package io.github.inoutch.kotchan.core.graphic.compatible.buffer

import io.github.inoutch.kotlin.gl.api.GL_DYNAMIC_DRAW
import io.github.inoutch.kotlin.gl.api.GL_STATIC_DRAW
import io.github.inoutch.kotlin.gl.api.GL_STREAM_DRAW
import io.github.inoutch.kotlin.gl.api.GLint
import io.github.inoutch.kotlin.vulkan.api.VkBufferUsageFlagBits

enum class BufferStorageMode(val glParam: GLint) {
    Static(GL_STATIC_DRAW),
    Dynamic(GL_DYNAMIC_DRAW),
    Stream(GL_STREAM_DRAW),
}
