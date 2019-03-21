package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.stringsToNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkInstanceCreateInfo.copyToNative(native: org.lwjgl.vulkan.VkInstanceCreateInfo, memScope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .pApplicationInfo(applicationInfo.toNative(memScope))
            .ppEnabledExtensionNames(enabledExtensionNames.stringsToNative(memScope))
            .ppEnabledLayerNames(enabledLayerNames.stringsToNative(memScope))
}

fun VkInstanceCreateInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkInstanceCreateInfo =
        memScope.add(org.lwjgl.vulkan.VkInstanceCreateInfo.calloc()
                .also { copyToNative(it, memScope) })
