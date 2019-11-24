package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkVertexInputAttributeDescription.copyToNative(native: org.lwjgl.vulkan.VkVertexInputAttributeDescription) {
    native.location(location)
            .binding(binding)
            .format(format.value)
            .offset(offset)
}

fun VkVertexInputAttributeDescription.toNative(scope: MemScope): org.lwjgl.vulkan.VkVertexInputAttributeDescription =
        scope.add(org.lwjgl.vulkan.VkVertexInputAttributeDescription.calloc()
                .also { copyToNative(it) })

fun List<VkVertexInputAttributeDescription>.toNative(scope: MemScope):
        org.lwjgl.vulkan.VkVertexInputAttributeDescription.Buffer? =
        if (isEmpty()) null else scope.add(org.lwjgl.vulkan.VkVertexInputAttributeDescription.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
