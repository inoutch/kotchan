package com.example.app

import android.content.Context
import android.opengl.GLSurfaceView

import android.view.MotionEvent


class SurfaceView(context: Context?) : GLSurfaceView(context)
{


    init {
        setEGLContextClientVersion(3)
        val myRenderer = Renderer(context)
        setRenderer(myRenderer)
    }
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return false
    }
}