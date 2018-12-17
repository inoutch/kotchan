package io.github.inoutch.kotchan.android

import android.opengl.GLSurfaceView
import android.util.DisplayMetrics
import android.view.WindowManager
import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.utility.type.Point
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class KotchanRenderer(private val config: KotchanEngine.Config, val windowManager: WindowManager) : GLSurfaceView.Renderer {
    lateinit var core: KotchanCore

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        val display = windowManager.defaultDisplay
        val dispMet = DisplayMetrics()
        display.getRealMetrics(dispMet)

        core = KotchanCore(this.config, Point(dispMet.widthPixels, dispMet.heightPixels))
        core.init()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) = core.reshape(0, 0, width, height)

    override fun onDrawFrame(gl: GL10?) = core.draw()
}