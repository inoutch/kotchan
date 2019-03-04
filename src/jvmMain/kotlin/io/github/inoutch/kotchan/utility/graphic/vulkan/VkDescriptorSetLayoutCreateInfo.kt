package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkDescriptorSetLayoutCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkDescriptorSetLayoutCreateInfo,
        memScope: MemScope) {
    native.pBindings(bindings.toNative(memScope))
}

fun VkDescriptorSetLayoutCreateInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkDescriptorSetLayoutCreateInfo =
        org.lwjgl.vulkan.VkDescriptorSetLayoutCreateInfo.calloc().also { copyToNative(it, memScope) }
