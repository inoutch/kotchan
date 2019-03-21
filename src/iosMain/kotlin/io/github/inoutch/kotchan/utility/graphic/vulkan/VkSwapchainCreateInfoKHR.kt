package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR

@ExperimentalUnsignedTypes
fun VkSwapchainCreateInfoKHR.copyToNative(
        native: vulkan.VkSwapchainCreateInfoKHR,
        scope: MemScope) {
    native.sType = VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR
    native.pNext = null
    native.flags = flags.toUInt()
    native.surface = surface.native
    native.minImageCount = minImageCount.toUInt()
    native.imageFormat = imageFormat.value.toUInt()
    native.imageColorSpace = imageColorSpace.value.toUInt()
    imageExtent.copyToNative(native.imageExtent)
    native.imageArrayLayers = imageArrayLayers.toUInt()
    native.imageUsage = imageUsage.sumBy { it.value }.toUInt()
    native.imageSharingMode = imageSharingMode.value.toUInt()
    native.queueFamilyIndexCount = queueFamilyIndices?.size?.toUInt() ?: 0u
    native.pQueueFamilyIndices = queueFamilyIndices?.map { it.toUInt() }?.toNative(scope)
    native.preTransform = preTransform.value.toUInt()
    native.compositeAlpha = compositeAlpha.value.toUInt()
    native.presentMode = presentMode.value.toUInt()
    native.clipped = clipped.toVkBool32()
    native.oldSwapchain = oldSwapchain?.native
}

@ExperimentalUnsignedTypes
fun VkSwapchainCreateInfoKHR.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkSwapchainCreateInfoKHR>()
                .also { copyToNative(it, scope) }.ptr
