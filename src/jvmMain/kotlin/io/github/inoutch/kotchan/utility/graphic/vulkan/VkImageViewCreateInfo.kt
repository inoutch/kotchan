package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkImageViewCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkImageViewCreateInfo,
        scope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .image(image.native)
            .viewType(viewType.value)
            .format(format.value)
            .components(components.toNative(scope))
            .subresourceRange(subresourceRange.toNative(scope))
}

fun VkImageViewCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkImageViewCreateInfo =
        scope.add(org.lwjgl.vulkan.VkImageViewCreateInfo.calloc()
                .also { copyToNative(it, scope) })
