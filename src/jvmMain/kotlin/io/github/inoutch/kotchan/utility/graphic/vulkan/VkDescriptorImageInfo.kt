package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkDescriptorImageInfo.copyToNative(native: org.lwjgl.vulkan.VkDescriptorImageInfo) {
    native.sampler(sampler.native)
            .imageView(imageView.native)
            .imageLayout(imageLayout.value)
}

fun List<VkDescriptorImageInfo>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.add(org.lwjgl.vulkan.VkDescriptorImageInfo.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
