package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkVertexInputBindingDescription.copyToNative(native: org.lwjgl.vulkan.VkVertexInputBindingDescription) {
    native.binding(binding)
            .stride(stride)
            .inputRate(inputRate.value)
}

fun VkVertexInputBindingDescription.toNative(scope: MemScope): org.lwjgl.vulkan.VkVertexInputBindingDescription =
        scope.add(org.lwjgl.vulkan.VkVertexInputBindingDescription.calloc()
                .also { copyToNative(it) })

fun List<VkVertexInputBindingDescription>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.add(org.lwjgl.vulkan.VkVertexInputBindingDescription.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
