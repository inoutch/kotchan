package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkDescriptorSetLayoutBinding.copyToNative(
        native: vulkan.VkDescriptorSetLayoutBinding,
        scope: MemScope) {
    native.binding = binding.toUInt()
    native.descriptorType = descriptorType.value.toUInt()
    native.descriptorCount = descriptorCount.toUInt()
    native.stageFlags = stageFlags.sumBy { it.value }.toUInt()
    native.pImmutableSamplers = immutableSamplers?.let { listOf(it.native) }?.toNative(scope)
}

@ExperimentalUnsignedTypes
fun List<VkDescriptorSetLayoutBinding>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkDescriptorSetLayoutBinding>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } }
