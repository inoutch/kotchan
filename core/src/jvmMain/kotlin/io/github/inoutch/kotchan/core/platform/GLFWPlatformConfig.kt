package io.github.inoutch.kotchan.core.platform

import io.github.inoutch.kotchan.core.KotchanPlatformConfig
import io.github.inoutch.kotchan.core.graphic.compatible.vk.VulkanPlatformConfig
import io.github.inoutch.kotchan.math.Vector2I

open class GLFWPlatformConfig : KotchanPlatformConfig {
    open val windowTitle: String? = null
    open val windowSize: Vector2I? = null
    open val resizable = false
    open val vulkanConfig: VulkanPlatformConfig = VulkanPlatformConfig()
}
