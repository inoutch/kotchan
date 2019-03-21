package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10
import org.lwjgl.vulkan.VK10.vkDestroyInstance

actual class VkInstance : Disposable {

    lateinit var native: org.lwjgl.vulkan.VkInstance
        private set

    fun init(nativeInstance: org.lwjgl.vulkan.VkInstance) {
        this.native = nativeInstance
    }

    override fun dispose() {
        vkDestroyInstance(native, null)
    }
}

actual fun vkCreateInstance(createInfo: VkInstanceCreateInfo) = memScoped {
    val native = allocPointer()
    val nativeCreateInfo = createInfo.toNative(this)

    checkError(VK10.vkCreateInstance(nativeCreateInfo, null, native))

    VkInstance().apply { init(org.lwjgl.vulkan.VkInstance(native.get(0), nativeCreateInfo)) }
}
