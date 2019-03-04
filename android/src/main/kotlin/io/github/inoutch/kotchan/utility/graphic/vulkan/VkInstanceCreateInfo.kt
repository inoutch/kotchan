package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkInstanceCreateInfo actual constructor(applicationInfo: VkApplicationInfo, enabledLayerNames: List<String>, enabledExtensionNames: List<String>) : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
