package io.github.inoutch.kotchan.android

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.WindowManager
import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.core.controller.touch.TouchEvent
import io.github.inoutch.kotchan.utility.type.Point

@SuppressLint("ViewConstructor")
class KotchanSurfaceView(config: KotchanEngine.Config, context: Context) : GLSurfaceView(context) {

    private val touchEvents = mutableMapOf<Int, TouchEvent>()

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val renderer = KotchanRenderer(config, windowManager)

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
        renderer.core.touchEmitter.onTouchesBegan(list)
    }

    private fun onTouchesMoved(event: MotionEvent) {
        val list = eventsToList(event)
                .mapNotNull {
                    touchEvents[it.first]?.point = it.second
                    touchEvents[it.first]
                }
        renderer.core.touchEmitter.onTouchesMoved(list)
    }

    private fun onTouchesEnded(event: MotionEvent) {
        val events = eventsToList(event)
        val list = events.mapNotNull {
            touchEvents[it.first]
        }
        renderer.core.touchEmitter.onTouchesEnded(list)
        events.forEach { touchEvents.remove(it.first) }
    }

    private fun onTouchesCancelled() {
        renderer.core.touchEmitter.onTouchesCancelled()
        touchEvents.clear()
    }

    private fun eventsToList(event: MotionEvent): List<Pair<Int, Point>> {
        val actionIndex = event.actionIndex
        val pid = event.findPointerIndex(actionIndex)
        return listOf(Pair(pid, Point(event.getX(actionIndex), height - event.getY(actionIndex))))
    }
}