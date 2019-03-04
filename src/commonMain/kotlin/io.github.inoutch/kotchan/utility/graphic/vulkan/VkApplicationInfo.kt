package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.type.Version

data class VkApplicationInfo(
        val applicationName: String,
        val applicationVersion: Version,
        val engineName: String,
        val engineVersion: Version,
        val apiVersion: Version)
