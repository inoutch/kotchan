package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_SUBMIT_INFO

@ExperimentalUnsignedTypes
fun VkSubmitInfo.copyToNative(
        native: vulkan.VkSubmitInfo,
        scope: MemScope) {
    native.sType = VK_STRUCTURE_TYPE_SUBMIT_INFO
    native.waitSemaphoreCount = waitSemaphores.size.toUInt()
    native.pWaitSemaphores = waitSemaphores.map { it.native }.toNative(scope)
    native.pWaitDstStageMask = listOf(waitDstStageMask.sumBy { it.value }.toUInt()).toNative(scope)
    native.commandBufferCount = commandBuffers.size.toUInt()
    native.pCommandBuffers = commandBuffers.map { it.native }.toNative(scope)
    native.signalSemaphoreCount = signalSemaphores.size.toUInt()
    native.pSignalSemaphores = signalSemaphores.map { it.native }.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkSubmitInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkSubmitInfo>()
                .also { copyToNative(it, scope) }.ptr

@ExperimentalUnsignedTypes
fun List<VkSubmitInfo>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkSubmitInfo>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } }
