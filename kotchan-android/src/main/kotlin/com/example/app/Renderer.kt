package com.example.app

import android.opengl.GLSurfaceView
import android.util.DisplayMetrics
import android.view.WindowManager
import application.AppConfig
import kotchan.Engine
import utility.type.Vector2
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class Renderer(val windowManager: WindowManager) : GLSurfaceView.Renderer {
    var engine: Engine? = null
    var width: Float = 0.0f
    var height: Float = 0.0f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val display = windowManager.defaultDisplay
        val dispMet = DisplayMetrics()
        display.getRealMetrics(dispMet)
        width = dispMet.widthPixels.toFloat()
        height = dispMet.heightPixels.toFloat()
        engine = Engine()
        engine?.init(Vector2(width, height), AppConfig.SCREEN_SIZE)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        engine?.reshape(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        engine?.draw()
    }
}