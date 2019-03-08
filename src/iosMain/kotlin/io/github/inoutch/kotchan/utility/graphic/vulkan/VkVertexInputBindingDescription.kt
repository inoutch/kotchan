package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkVertexInputBindingDescription.copyToNative(native: vulkan.VkVertexInputBindingDescription) {
    native.binding = binding.toUInt()
    native.stride = stride.toUInt()
    native.inputRate = inputRate.value.toUInt()
}

@ExperimentalUnsignedTypes
fun List<VkVertexInputBindingDescription>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkVertexInputBindingDescription>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
