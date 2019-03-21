package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import io.github.inoutch.kotchan.utility.type.Version
import org.lwjgl.system.MemoryUtil
import org.lwjgl.vulkan.VK10

fun VkApplicationInfo.copyToNative(native: org.lwjgl.vulkan.VkApplicationInfo) {
    native.sType(VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .pApplicationName(MemoryUtil.memUTF8(applicationName))
            .applicationVersion(convertVersion(applicationVersion))
            .pEngineName(MemoryUtil.memUTF8(engineName))
            .engineVersion(convertVersion(engineVersion))
            .apiVersion(convertVersion(apiVersion))
}

fun VkApplicationInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkApplicationInfo =
        memScope.add(org.lwjgl.vulkan.VkApplicationInfo.calloc()
                .also { copyToNative(it) })

fun convertVersion(version: Version) = VK10.VK_MAKE_VERSION(version.major, version.minor, version.patch)
