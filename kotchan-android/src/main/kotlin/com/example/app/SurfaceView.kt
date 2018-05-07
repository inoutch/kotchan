package com.example.app

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowManager
import application.AppConfig
import kotchan.Engine
import kotchan.controller.touch.TouchEvent

import utility.type.Vector2


class SurfaceView(context: Context?) : GLSurfaceView(context) {
    private val touchEvents = mutableMapOf<Int, TouchEvent>()
    private var engine: Engine = Engine()
    private var width: Float
    private var height: Float

    init {
        setEGLContextClientVersion(3)

        val winMan: WindowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = winMan.defaultDisplay
        val dispMet = DisplayMetrics()
        display.getRealMetrics(dispMet)
        width = dispMet.widthPixels.toFloat()
        height = dispMet.heightPixels.toFloat()
        engine.init(Vector2(width, height), AppConfig.SCREEN_SIZE)
        setRenderer(Renderer(engine))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventAction = event.actionMasked
        when (eventAction) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> this.onTouchesBegan(event)
            MotionEvent.ACTION_MOVE -> this.onTouchesMoved(event)
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> this.onTouchesEnded(event)
            MotionEvent.ACTION_CANCEL -> this.onTouchesCancelled()
        }
        return true
    }

    private fun onTouchesBegan(event: MotionEvent) {
        val list = eventsToList(event).map {
            val touchEvent = TouchEvent(it.second)
            touchEvents[it.first] = touchEvent
            touchEvent
        }
        engine.touchEmitter.onTouchesBegan(list)
    }

    private fun onTouchesMoved(event: MotionEvent) {
        val list = eventsToList(event).mapNotNull {
            touchEvents[it.first]
        }
        engine.touchEmitter.onTouchesMoved(list)
    }

    private fun onTouchesEnded(event: MotionEvent) {
        val list = eventsToList(event).mapNotNull {
            touchEvents[it.first]
        }
        engine.touchEmitter.onTouchesEnded(list)
    }

    private fun onTouchesCancelled() {
        engine.touchEmitter.onTouchesCancelled()
        touchEvents.clear()
    }

    private fun eventsToList(event: MotionEvent): List<Pair<Int, Vector2>> {
        val list = mutableListOf<Pair<Int, Vector2>>()
        val count = event.pointerCount
        for (i in 0 until count) {
            list.add(Pair(event.getPointerId(i), Vector2(event.getX(i), height - event.getY(i))))
        }
        return list
    }

}