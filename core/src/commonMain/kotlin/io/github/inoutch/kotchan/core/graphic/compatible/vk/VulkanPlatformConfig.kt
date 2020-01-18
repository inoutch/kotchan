package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotlin.vulkan.utility.Version

open class VulkanPlatformConfig {
    open val applicationName: String? = null
    open val version: Version = Version(1, 0, 0)
    open val enableLayerNames: List<String> = emptyList()
    open val enableExtensionNames: List<String>? = null
    open val engineName: String? = null
    open val engineVersion: Version = Version(1, 0, 0)
    open val apiVersion: Version = Version(1, 0, 3)
    open val maxFrameInFlight = 3
}
