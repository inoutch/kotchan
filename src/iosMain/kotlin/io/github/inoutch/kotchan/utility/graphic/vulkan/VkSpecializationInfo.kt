package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import kotlinx.cinterop.refTo

@ExperimentalUnsignedTypes
fun VkSpecializationInfo.copyToNative(
        native: vulkan.VkSpecializationInfo,
        scope: MemScope) {
    native.mapEntryCount = mapEntities.size.toUInt()
    native.pMapEntries = mapEntities.toNative(scope)
    native.dataSize = data.size.toULong()
    native.pData = data.refTo(0).getPointer(scope).rawValue.toLong().toCPointer()
}

@ExperimentalUnsignedTypes
fun VkSpecializationInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkSpecializationInfo>().also { copyToNative(it, scope) }.ptr
