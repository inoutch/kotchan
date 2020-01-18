package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.math.Vector2I
import kotlin.native.concurrent.ThreadLocal

class KotchanConfig {
    @ThreadLocal
    val windowSize: Vector2I = Vector2I.Zero

    @ThreadLocal
    val viewportSize: Vector2I = Vector2I.Zero
}
