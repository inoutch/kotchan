package io.github.inoutch.kotchan.core.platform

import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.core.KotchanPlatformLauncher
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.graphic.compatible.gl.GLContext
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.gl.api.gl
import kotlin.browser.document
import kotlin.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement

class WebGLLauncher(
    private val engine: KotchanEngine,
    config: WebGLPlatformConfig
): KotchanPlatformLauncher {
    val context: GLContext

    private var thrownError: Error? = null

    private var currentTime = 0.0

    init {
        val configCanvas = config.canvas
        val canvas = checkNotNull(configCanvas
                ?: document.getElementById(config.canvasId) as? HTMLCanvasElement) { "Canvas not found" }
        canvas.width = config.canvasSize?.x ?: engine.startupConfig.windowSize.x
        canvas.height = config.canvasSize?.y ?: engine.startupConfig.windowSize.y
        engine.initSize(
                Vector2I(canvas.width, canvas.height),
                Vector2I(canvas.width, canvas.height)
        )

        val context = canvas.getContext("webgl") ?: canvas.getContext("experimental-webgl")
        check(context is WebGLRenderingContext) { "Invalid webgl rendering context" }
        gl.setContext(context)
        this.context = GLContext()
    }

    override fun getGraphics(): Context {
        return context
    }

    override suspend fun startAnimation() {
        val launcher = this
        fun renderLoop() {
            window.requestAnimationFrame {
                val delta = it - currentTime
                currentTime = it
                GlobalScope.launch(Dispatchers.Unconfined) {
                    try {
                        engine.render(delta.toFloat() * 0.001f)
                    } catch (e: Error) {
                        launcher.thrownError = e
                    }
                }
                val thrownError = launcher.thrownError
                if (thrownError == null) {
                    renderLoop()
                } else {
                    throw thrownError
                }
            }
        }
        renderLoop()
    }
}
