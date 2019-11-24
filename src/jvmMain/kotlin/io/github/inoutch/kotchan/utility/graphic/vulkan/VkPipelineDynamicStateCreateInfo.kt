package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkPipelineDynamicStateCreateInfo.copyToNative(
    native: org.lwjgl.vulkan.VkPipelineDynamicStateCreateInfo,
    scope: MemScope
) {
    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_DYNAMIC_STATE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .pDynamicStates(if (dynamicStates.isEmpty())
                null
            else dynamicStates.map { it.value }.toIntArray().toNative(scope))
}

fun VkPipelineDynamicStateCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkPipelineDynamicStateCreateInfo =
        scope.add(org.lwjgl.vulkan.VkPipelineDynamicStateCreateInfo.calloc()
                .also { copyToNative(it, scope) })
