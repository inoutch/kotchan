package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkShaderModuleCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkShaderModuleCreateInfo,
        scope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .pCode(code.toNative(scope))
}

fun VkShaderModuleCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkShaderModuleCreateInfo =
        scope.add(org.lwjgl.vulkan.VkShaderModuleCreateInfo.calloc()
                .also { copyToNative(it, scope) })
