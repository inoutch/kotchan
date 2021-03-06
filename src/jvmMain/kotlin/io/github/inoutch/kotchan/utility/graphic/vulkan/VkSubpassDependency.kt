package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkSubpassDependency.copyToNative(native: org.lwjgl.vulkan.VkSubpassDependency) {
    native.srcSubpass(srcSubpassIndex)
            .dstSubpass(dstSubpassIndex)
            .srcStageMask(srcStageMask.sumBy { it.value })
            .dstStageMask(dstStageMask.sumBy { it.value })
            .srcAccessMask(srcAccessMask.sumBy { it.value })
            .dstAccessMask(dstAccessMask.sumBy { it.value })
            .dependencyFlags(dependencyFlags.sumBy { it.value })
}

fun List<VkSubpassDependency>.toNative(memScope: MemScope) =
        if (isEmpty()) null else memScope.add(org.lwjgl.vulkan.VkSubpassDependency.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
