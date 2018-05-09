package com.example.app

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.WindowManager
import kotchan.controller.touch.TouchEvent

import utility.type.Vector2

class SurfaceView(context: Context?) : GLSurfaceView(context) {
    private val touchEvents = mutableMapOf<Int, TouchEvent>()
    private val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val renderer = Renderer(windowManager)

    init {
        setEGLContextClientVersion(3)
        setRenderer(renderer)
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
        renderer.engine?.touchEmitter?.onTouchesBegan(list)
    }

    private fun onTouchesMoved(event: MotionEvent) {
        val list = eventsToList(event).mapNotNull {
            touchEvents[it.first]
        }
        renderer.engine?.touchEmitter?.onTouchesMoved(list)
    }

    private fun onTouchesEnded(event: MotionEvent) {
        val events = eventsToList(event)
        val list = events.mapNotNull {
            touchEvents[it.first]
        }
        renderer.engine?.touchEmitter?.onTouchesEnded(list)
        events.forEach { touchEvents.remove(it.first) }
    }

    private fun onTouchesCancelled() {
        renderer.engine?.touchEmitter?.onTouchesCancelled()
        touchEvents.clear()
    }

    private fun eventsToList(event: MotionEvent): List<Pair<Int, Vector2>> {
        val actionIndex = event.actionIndex
        val pid = event.findPointerIndex(actionIndex)
        return listOf(Pair(pid, Vector2(event.getX(actionIndex), height - event.getY(actionIndex))))
    }
}