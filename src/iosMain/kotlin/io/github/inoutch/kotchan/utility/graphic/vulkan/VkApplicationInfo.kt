package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkApplicationInfo.copyToNative(native: vulkan.VkApplicationInfo, scope: MemScope) {
    native.sType = vulkan.VK_STRUCTURE_TYPE_APPLICATION_INFO
    native.pNext = null
    native.pApplicationName = applicationName.cstr.getPointer(scope)
    native.applicationVersion = applicationVersion.toNative()
    native.pEngineName = engineName.cstr.getPointer(scope)
    native.engineVersion = engineVersion.toNative()
    native.apiVersion = applicationVersion.toNative()
}

@ExperimentalUnsignedTypes
fun VkApplicationInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkApplicationInfo>().also { copyToNative(it, scope) }.ptr
