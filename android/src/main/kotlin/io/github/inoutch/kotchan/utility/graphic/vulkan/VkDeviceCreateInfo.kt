package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkDeviceCreateInfo actual constructor(queueCreateInfos: List<VkDeviceQueueCreateInfo>, enabledExtensionNames: List<String>, enabledLayerNames: List<String>) : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
