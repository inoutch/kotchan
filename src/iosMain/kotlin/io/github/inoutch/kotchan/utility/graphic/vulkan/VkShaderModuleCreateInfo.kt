package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkShaderModuleCreateInfo.copyToNative(
        native: vulkan.VkShaderModuleCreateInfo,
        scope: MemScope) {
    native.sType = VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.codeSize = code.size.toULong()
    native.pCode = code.refTo(0).getPointer(scope).rawValue.toLong().toCPointer()
}

@ExperimentalUnsignedTypes
fun VkShaderModuleCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkShaderModuleCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
