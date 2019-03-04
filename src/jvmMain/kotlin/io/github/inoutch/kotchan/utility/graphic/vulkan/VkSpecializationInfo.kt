package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope

fun VkSpecializationInfo.copyToNative(
        native: org.lwjgl.vulkan.VkSpecializationInfo,
        scope: MemScope) {
    native.pMapEntries(mapEntities.toNative(scope))
            .pData(data.toNative(scope))
}

fun VkSpecializationInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkSpecializationInfo =
        scope.add(org.lwjgl.vulkan.VkSpecializationInfo.calloc()
                .pMapEntries(mapEntities.toNative(scope)))
