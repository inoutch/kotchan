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


class Renderer(context: Context?): GLSurfaceView.Renderer{

    private var context = context
    private var engine : Engine? = null
    private var beforeMillis: Long = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        var winMan : WindowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var display = winMan.defaultDisplay
        var dispMet = DisplayMetrics()
        display.getRealMetrics(dispMet)
        val width = dispMet.widthPixels
        val height = dispMet.heightPixels
        engine = Engine()
        engine?.init(Vector2(width.toFloat(), height.toFloat()), AppConfig.SCREEN_SIZE)
        GLES30.glClearColor(1.0f,0.0f,0.0f,1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //engine?.reshape(x,y,width,height)
    }

    override fun onDrawFrame(gl: GL10?) {
        val millisPerFrame = System.currentTimeMillis() - beforeMillis
        beforeMillis = System.currentTimeMillis()
        engine?.draw(millisPerFrame.toFloat() / 1000.0f)
        assert( GLES30.glGetError() == GLES30.GL_NO_ERROR ){
        }
    }
}