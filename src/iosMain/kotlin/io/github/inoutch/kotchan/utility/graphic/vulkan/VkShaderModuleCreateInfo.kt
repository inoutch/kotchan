package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import platform.posix.uint32_tVar
import vulkan.VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkShaderModuleCreateInfo.copyToNative(native: vulkan.VkShaderModuleCreateInfo) {
    native.sType = VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.codeSize = code.size.toULong()
    @Suppress("UNCHECKED_CAST")
    native.pCode = code.refTo(0) as CPointer<uint32_tVar>
}

@ExperimentalUnsignedTypes
fun VkShaderModuleCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkShaderModuleCreateInfo>()
                .also { copyToNative(it) }.ptr
