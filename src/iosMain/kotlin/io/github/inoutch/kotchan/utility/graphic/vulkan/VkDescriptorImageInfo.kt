package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkDescriptorImageInfo.copyToNative(native: vulkan.VkDescriptorImageInfo) {
    native.sampler = sampler.native
    native.imageView = imageView.native
    native.imageLayout = imageLayout.value.toUInt()
}

@ExperimentalUnsignedTypes
fun List<VkDescriptorImageInfo>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkDescriptorImageInfo>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
