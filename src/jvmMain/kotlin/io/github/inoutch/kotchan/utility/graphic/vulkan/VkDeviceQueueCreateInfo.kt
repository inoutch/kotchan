package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkDeviceQueueCreateInfo.copyToNative(
    native: org.lwjgl.vulkan.VkDeviceQueueCreateInfo,
    scope: MemScope
) {
    native.sType(VK10.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .queueFamilyIndex(queueFamilyIndex)
            .pQueuePriorities(queuePriorities.toFloatArray().toNative(scope))
}

fun List<VkDeviceQueueCreateInfo>.toNative(scope: MemScope): org.lwjgl.vulkan.VkDeviceQueueCreateInfo.Buffer {
    return scope.add(org.lwjgl.vulkan.VkDeviceQueueCreateInfo.calloc(size)
            .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } })
}
