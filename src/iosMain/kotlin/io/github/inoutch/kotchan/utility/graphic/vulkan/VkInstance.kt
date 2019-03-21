package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*
import vulkan.VkInstanceVar

actual class VkInstance actual constructor() : Disposable {
    lateinit var native: vulkan.VkInstance
        private set

    fun init(nativeInstance: vulkan.VkInstance) {
        this.native = nativeInstance
    }

    override fun dispose() {
        vulkan.vkDestroyInstance(native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateInstance(createInfo: VkInstanceCreateInfo) = memScoped {
    val native = alloc<VkInstanceVar>()

    checkError(vulkan.vkCreateInstance(createInfo.toNative(this), null, native.ptr))

    VkInstance().apply { init(native.value ?: throw VkNullError("instance")) }
}
