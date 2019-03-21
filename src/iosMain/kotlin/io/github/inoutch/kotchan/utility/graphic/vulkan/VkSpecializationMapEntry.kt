package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkSpecializationMapEntry.copyToNative(native: vulkan.VkSpecializationMapEntry) {
    native.constantID = constantID.toUInt()
    native.offset = offset.toUInt()
    native.size = size.toULong()
}

@ExperimentalUnsignedTypes
fun List<VkSpecializationMapEntry>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkSpecializationMapEntry>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
