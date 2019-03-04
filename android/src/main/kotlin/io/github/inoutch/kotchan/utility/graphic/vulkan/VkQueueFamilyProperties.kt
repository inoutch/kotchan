package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkQueueFamilyProperties : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun isGraphicQueueFamily(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice: VkPhysicalDevice): List<VkQueueFamilyProperties> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
