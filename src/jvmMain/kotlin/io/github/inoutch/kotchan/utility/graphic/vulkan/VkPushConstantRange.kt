package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkPushConstantRange.copyToNative(native: org.lwjgl.vulkan.VkPushConstantRange) {
    native.stageFlags(stageFlags.sumBy { it.value })
            .offset(offset)
            .size(size)
}

fun VkPushConstantRange.toNative(memScope: MemScope): org.lwjgl.vulkan.VkPushConstantRange =
        memScope.add(org.lwjgl.vulkan.VkPushConstantRange.calloc()
                .also { copyToNative(it) })

fun List<VkPushConstantRange>.toNative(memScope: MemScope): org.lwjgl.vulkan.VkPushConstantRange.Buffer? =
        if (isEmpty()) null else memScope.add(org.lwjgl.vulkan.VkPushConstantRange.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
