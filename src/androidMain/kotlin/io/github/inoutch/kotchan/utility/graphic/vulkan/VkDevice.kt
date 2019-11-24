package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkDevice : Disposable {
    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkCreateDevice(physicalDevice: VkPhysicalDevice, createInfo: VkDeviceCreateInfo): VkDevice {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}

actual fun vkDeviceWaitIdle(device: VkDevice) {
    // no implementation
}
