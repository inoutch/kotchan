package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.KHRSwapchain
import org.lwjgl.vulkan.VK10

fun VkPresentInfoKHR.copyToNative(
    native: org.lwjgl.vulkan.VkPresentInfoKHR,
    scope: MemScope
) {

    native.sType(KHRSwapchain.VK_STRUCTURE_TYPE_PRESENT_INFO_KHR)
            .pNext(VK10.VK_NULL_HANDLE)
            .pWaitSemaphores(waitSemaphores.map { it.native }.toLongArray().toNative(scope))
            .swapchainCount(swapchains.size)
            .pSwapchains(swapchains.map { it.native }.toLongArray().toNative(scope))
            .pImageIndices(imageIndices.toIntArray().toNative(scope))
            .pResults(results?.map { it.value }?.toIntArray()?.toNative(scope))
}

fun VkPresentInfoKHR.toNative(scope: MemScope): org.lwjgl.vulkan.VkPresentInfoKHR =
        scope.add(org.lwjgl.vulkan.VkPresentInfoKHR.calloc()
                .also { copyToNative(it, scope) })
