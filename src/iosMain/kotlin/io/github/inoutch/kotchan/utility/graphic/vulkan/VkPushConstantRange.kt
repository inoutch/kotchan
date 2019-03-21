package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkPushConstantRange.copyToNative(native: vulkan.VkPushConstantRange) {
    native.stageFlags = stageFlags.sumBy { it.value }.toUInt()
    native.offset = offset.toUInt()
    native.size = offset.toUInt()
}

@ExperimentalUnsignedTypes
fun List<VkPushConstantRange>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkPushConstantRange>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
