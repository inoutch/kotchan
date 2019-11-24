package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkSurface : Disposable {
    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkGetPhysicalDeviceSurfaceSupportKHR(physicalDevice: VkPhysicalDevice, queueFamilyIndex: Int, surface: VkSurface): Boolean {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}

actual fun vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice: VkPhysicalDevice, surface: VkSurface): List<VkPresentModeKHR> {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}
