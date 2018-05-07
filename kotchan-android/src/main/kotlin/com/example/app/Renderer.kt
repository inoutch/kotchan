package com.example.app

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.DisplayMetrics
import android.view.WindowManager
import application.AppConfig
import kotchan.Engine
import utility.type.Vector2
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class Renderer(private val engine: Engine) : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {}

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        engine.reshape(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        engine.draw()
    }
}