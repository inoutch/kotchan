package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkSurfaceFormatKHR : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual val format: VkFormat
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    actual val colorSpace: VkColorSpaceKHR
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}

actual fun vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice: VkPhysicalDevice, surface: VkSurface): List<VkSurfaceFormatKHR> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
