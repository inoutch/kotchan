package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkDescriptorPoolSize.copyToNative(native: vulkan.VkDescriptorPoolSize) {
    native.type = type.value.toUInt()
    native.descriptorCount = descriptorCount.toUInt()
}

@ExperimentalUnsignedTypes
fun List<VkDescriptorPoolSize>.toNative(scope: MemScope) =
        scope.allocArray<vulkan.VkDescriptorPoolSize>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
