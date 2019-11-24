package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.KHRSwapchain
import org.lwjgl.vulkan.VK10

fun VkSwapchainCreateInfoKHR.copyToNative(
    native: org.lwjgl.vulkan.VkSwapchainCreateInfoKHR,
    scope: MemScope
) {
    native.sType(KHRSwapchain.VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .surface(surface.native)
            .minImageCount(minImageCount)
            .imageFormat(imageFormat.value)
            .imageColorSpace(imageColorSpace.value)
            .imageExtent(imageExtent.toNativeExtent(scope))
            .imageArrayLayers(imageArrayLayers)
            .imageUsage(imageUsage.sumBy { it.value })
            .imageSharingMode(imageSharingMode.value)
            .pQueueFamilyIndices(queueFamilyIndices?.toIntArray()?.toNative(scope))
            .preTransform(preTransform.value)
            .compositeAlpha(compositeAlpha.value)
            .presentMode(presentMode.value)
            .clipped(clipped)
            .oldSwapchain(oldSwapchain?.native ?: VK10.VK_NULL_HANDLE)
}

fun VkSwapchainCreateInfoKHR.toNative(scope: MemScope): org.lwjgl.vulkan.VkSwapchainCreateInfoKHR =
        scope.add(org.lwjgl.vulkan.VkSwapchainCreateInfoKHR.calloc()
                .also { copyToNative(it, scope) })
