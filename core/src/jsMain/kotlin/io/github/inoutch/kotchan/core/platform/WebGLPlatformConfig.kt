package io.github.inoutch.kotchan.core.platform

import io.github.inoutch.kotchan.core.KotchanPlatformConfig
import io.github.inoutch.kotchan.math.Vector2I
import org.w3c.dom.HTMLCanvasElement

open class WebGLPlatformConfig : KotchanPlatformConfig {
    open val canvas: HTMLCanvasElement? = null
    open val canvasId: String = "canvas"
    open val canvasSize: Vector2I? = null
}
