package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkImageCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkImageCreateInfo,
        scope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .imageType(imageType.value)
            .extent(extent.toNative(scope))
            .format(format.value)
            .mipLevels(mipLevels)
            .arrayLayers(arrayLayers)
            .samples(samples.value)
            .tiling(tiling.value)
            .usage(usage.sumBy { it.value })
            .sharingMode(sharingMode.value)
            .pQueueFamilyIndices(queueFamilyIndices.toIntArray().toNative(scope))
            .initialLayout(initialLayout.value)
}

fun VkImageCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkImageCreateInfo =
        scope.add(org.lwjgl.vulkan.VkImageCreateInfo.calloc()
                .also { copyToNative(it, scope) })
