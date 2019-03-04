package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.pointerBuffersToNative
import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkSubmitInfo.copyToNative(
        native: org.lwjgl.vulkan.VkSubmitInfo,
        scope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_SUBMIT_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .waitSemaphoreCount(waitSemaphores.size)
            .pWaitDstStageMask(waitDstStageMask.map { it.value }.toIntArray().toNative(scope))
            .pWaitSemaphores(waitSemaphores.map { it.native }.toLongArray().toNative(scope))
            .pCommandBuffers(commandBuffers.map { it.native }.pointerBuffersToNative(scope))
            .pSignalSemaphores(signalSemaphores.map { it.native }.toLongArray().toNative(scope))
}

fun List<VkSubmitInfo>.toNative(scope: MemScope): org.lwjgl.vulkan.VkSubmitInfo.Buffer =
        scope.add(org.lwjgl.vulkan.VkSubmitInfo.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } })
