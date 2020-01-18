package io.github.inoutch.kotchan.core.platform

import io.github.inoutch.kotchan.core.graphic.compatible.vk.VulkanPlatformConfig

open class KotchanViewControllerConfig {
    open val vulkanConfig: VulkanPlatformConfig = object : VulkanPlatformConfig() {
        override val enableExtensionNames: List<String> = listOf("VK_KHR_surface", "VK_MVK_ios_surface")
    }
}
