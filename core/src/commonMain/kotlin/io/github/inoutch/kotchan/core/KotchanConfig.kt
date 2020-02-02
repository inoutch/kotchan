package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.graphic.compatible.vk.VKContext
import io.github.inoutch.kotchan.math.Vector2I
import kotlin.native.concurrent.ThreadLocal

class KotchanConfig(platform: KotchanPlatform) {
    @ThreadLocal
    val windowSize: Vector2I = Vector2I.Zero

    @ThreadLocal
    val viewportSize: Vector2I = Vector2I.Zero

    val useVulkan = platform.graphic is VKContext
}
