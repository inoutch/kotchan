package io.github.inoutch.kotchan.core.platform

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.util.DisplayMetrics
import android.view.WindowManager
import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.core.KotchanGlobalContext
import io.github.inoutch.kotchan.core.KotchanPlatformConfig
import io.github.inoutch.kotchan.core.KotchanStartupConfig
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.utility.Timer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@SuppressLint("ViewConstructor")
class KotchanGLSurfaceView(
        private val startupConfig: KotchanStartupConfig,
        private val platformConfig: KotchanPlatformConfig?,
        context: Context
) : GLSurfaceView(context), GLSurfaceView.Renderer {
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val dispatcher = DispatcherGL(this)

    private lateinit var engine: KotchanEngine

    private var beforeMillis = 0L

    init {
        setEGLContextClientVersion(2)
        setRenderer(this)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getRealMetrics(displayMetrics)

        engine = KotchanEngine(startupConfig)
        engine.initSize(
                Vector2I(displayMetrics.widthPixels, displayMetrics.heightPixels),
                Vector2I(displayMetrics.widthPixels, displayMetrics.heightPixels)
        )

        val applicationContext = this.context.applicationContext
        runBlocking {
            engine.run(platformConfig)

            val filePlatform = KotchanFilePlatform(applicationContext.assets,  applicationContext.filesDir.path)
            KotchanGlobalContext.file.initialize(filePlatform)

            beforeMillis = Timer.milliseconds()
        }
    }

    override fun onDrawFrame(p0: GL10?) {
        GlobalScope.launch(dispatcher) { engine.render(calcDelta()) }
    }

    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
        val size = Vector2I(p1, p2)
        engine.resize(size, size)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        KotchanGlobalContext.file.clear()
    }

    private fun calcDelta(): Float {
        val now = Timer.milliseconds()
        val millisPerFrame = now - beforeMillis

        beforeMillis = now

        return millisPerFrame.toFloat() / 1000.0f
    }
}
