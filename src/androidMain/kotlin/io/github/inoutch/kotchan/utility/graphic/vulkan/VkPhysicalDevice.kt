package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkPhysicalDevice actual constructor() : Disposable {
    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkEnumeratePhysicalDevices(instance: VkInstance): List<VkPhysicalDevice> {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}
