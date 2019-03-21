package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkBufferImageCopy.copyToNative(
        native: org.lwjgl.vulkan.VkBufferImageCopy,
        scope: MemScope) {
    native.bufferOffset(bufferOffset)
            .bufferRowLength(bufferRowLength)
            .bufferImageHeight(bufferImageHeight)
            .imageSubresource(imageSubresource.toNative(scope))
            .imageOffset(imageOffset.toNative(scope))
            .imageExtent(imageExtent.toNative(scope))
}

fun VkBufferImageCopy.toNative(scope: MemScope): org.lwjgl.vulkan.VkBufferImageCopy =
        scope.add(org.lwjgl.vulkan.VkBufferImageCopy.calloc()
                .also { copyToNative(it, scope) })

fun List<VkBufferImageCopy>.toNative(scope: MemScope): org.lwjgl.vulkan.VkBufferImageCopy.Buffer =
        scope.add(org.lwjgl.vulkan.VkBufferImageCopy.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } })
