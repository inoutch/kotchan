package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope

fun VkDescriptorSetLayoutBinding.toNative(memScope: MemScope): org.lwjgl.vulkan.VkDescriptorSetLayoutBinding =
        memScope.add(org.lwjgl.vulkan.VkDescriptorSetLayoutBinding
                .calloc()
                .also { copyToNative(it, memScope) })

fun VkDescriptorSetLayoutBinding.copyToNative(
        native: org.lwjgl.vulkan.VkDescriptorSetLayoutBinding,
        scope: MemScope) {
    native.binding(binding)
            .descriptorType(descriptorType.value)
            .descriptorCount(descriptorCount)
            .stageFlags(stageFlags.sumBy { it.value })
            .pImmutableSamplers(immutableSamplers?.let { longArrayOf(it.native).toNative(scope) })
}

fun List<VkDescriptorSetLayoutBinding>.toNative(memScope: MemScope):
        org.lwjgl.vulkan.VkDescriptorSetLayoutBinding.Buffer =
        memScope.add(org.lwjgl.vulkan.VkDescriptorSetLayoutBinding
                .calloc(this.size)
                .also { buffer -> forEachIndexed { index, x -> x.copyToNative(buffer[index], memScope) } })
