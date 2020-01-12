package io.github.inoutch.kotchan.core.platform

import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotlin.gl.api.gl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window

class WebGLLauncher(
        private val engine: KotchanEngine,
        config: WebGLPlatformConfig
) {
    init {
        val configCanvas = config.canvas
        val canvas = checkNotNull(configCanvas
                ?: document.getElementById(config.canvasId) as? HTMLCanvasElement) { "Canvas not found" }
        canvas.width = config.canvasSize?.x ?: engine.startupConfig.windowSize.x
        canvas.height = config.canvasSize?.y ?: engine.startupConfig.windowSize.y

        val context = canvas.getContext("webgl") ?: canvas.getContext("experimental-webgl")
        check(context is WebGLRenderingContext) { "Invalid webgl rendering context" }
        gl.setContext(context)
    }

    fun startAnimation() {
        fun renderLoop() {
            window.requestAnimationFrame {
                GlobalScope.launch(Dispatchers.Unconfined) {
                    engine.render(it.toFloat())
                }
                renderLoop()
            }
        }
        renderLoop()
    }
}
