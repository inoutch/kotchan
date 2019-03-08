package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkVertexInputAttributeDescription.copyToNative(native: vulkan.VkVertexInputAttributeDescription) {
    native.binding = binding.toUInt()
    native.format = format.value.toUInt()
    native.location = location.toUInt()
    native.offset = offset.toUInt()
}

@ExperimentalUnsignedTypes
fun List<VkVertexInputAttributeDescription>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkVertexInputAttributeDescription>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
