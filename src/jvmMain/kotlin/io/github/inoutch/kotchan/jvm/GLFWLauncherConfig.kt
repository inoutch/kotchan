package io.github.inoutch.kotchan.jvm

import io.github.inoutch.kotchan.math.Vector2I

interface GLFWLauncherConfig {
    val nativeScreenSize: Vector2I

    val useVulkanIfSupported: Boolean

    val windowName: String

    val fps: Int

    suspend fun render()

    suspend fun update()
}
