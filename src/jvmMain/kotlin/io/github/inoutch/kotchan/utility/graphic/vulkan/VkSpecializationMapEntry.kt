package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkSpecializationMapEntry.copyToNative(native: org.lwjgl.vulkan.VkSpecializationMapEntry) {
    native.constantID(constantID)
            .offset(offset)
            .size(size)
}

fun VkSpecializationMapEntry.toNative(memScope: MemScope): org.lwjgl.vulkan.VkSpecializationMapEntry =
        memScope.add(org.lwjgl.vulkan.VkSpecializationMapEntry.calloc()
                .also { copyToNative(it) })

fun List<VkSpecializationMapEntry>.toNative(scope: MemScope): org.lwjgl.vulkan.VkSpecializationMapEntry.Buffer? =
        if (isEmpty()) null else scope.add(org.lwjgl.vulkan.VkSpecializationMapEntry.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
