package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkImageCreateInfo.copyToNative(
    native: vulkan.VkImageCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.imageType = imageType.value.toUInt()
    extent.copyToNative(native.extent)
    native.format = format.value.toUInt()
    native.mipLevels = mipLevels.toUInt()
    native.arrayLayers = arrayLayers.toUInt()
    native.samples = samples.value.toUInt()
    native.tiling = tiling.value.toUInt()
    native.usage = usage.sumBy { it.value }.toUInt()
    native.sharingMode = sharingMode.value.toUInt()
    native.queueFamilyIndexCount = queueFamilyIndices.size.toUInt()
    native.pQueueFamilyIndices = queueFamilyIndices.map { it.toUInt() }.toNative(scope)
    native.initialLayout = initialLayout.value.toUInt()
}

@ExperimentalUnsignedTypes
fun VkImageCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkImageCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
