package com.example.app

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import kotchan.Engine
import kotchan.controller.touch.TouchEvent

import utility.type.Vector2


class SurfaceView(context: Context?) : GLSurfaceView(context)
{
    private val touchEvents = mutableMapOf<Int, TouchEvent>()
    private var engine: Engine = Engine()
    private var width : Float = 0.0f
    private var height : Float = 0.0f
    init {
        setEGLContextClientVersion(3)
        val myRenderer = Renderer(context)
        setRenderer(myRenderer)
        val winMan : WindowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = winMan.defaultDisplay
        val dispMet = DisplayMetrics()
        display.getRealMetrics(dispMet)
        width = dispMet.widthPixels.toFloat()
        height = dispMet.heightPixels.toFloat()
    }
    private fun debug(dlist: List<TouchEvent>){
        for((index,touchEvent) in dlist.withIndex()){
            Log.d("debug","index=$index,x=${touchEvent.point.x},y=${touchEvent.point.y}")
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val eventAction = e.actionMasked
        when(eventAction){
            MotionEvent.ACTION_DOWN,MotionEvent.ACTION_POINTER_DOWN -> {
                val pointerIndex = e.actionIndex
                val x = e.getX(pointerIndex)
                val y = e.getY(pointerIndex)
                val point= Vector2(x,height-y)
                val pointerid = e.getPointerId(pointerIndex)
                val touchEvent = TouchEvent(point)
                touchEvents[pointerid] = touchEvent
            //    debug(touchEvents.values.toList())
            //    engine.touchEmitter.onTouchesBegan(touchEvents.values.toList())}
                engine.touchEmitter.onTouchesBegan(listOf(touchEvent))
            }

            MotionEvent.ACTION_MOVE -> {
                val historySize = e.historySize
                for(h in 0 until historySize) {
                    val historyList = touchEvents.map { (pointerId, touchEvent) ->
                        val pointerIndex = e.findPointerIndex(pointerId)
                        val x = e.getHistoricalX(pointerIndex,h)
                        val y = e.getHistoricalY(pointerIndex,h)
                        touchEvent.point = Vector2(x, height-y)
                        touchEvent
                    }
             //       debug(historyList)
                    engine.touchEmitter.onTouchesMoved(historyList)
                }
                val currentList = touchEvents.map{(pointerId,touchEvent) ->
                    val pointerIndex = e.findPointerIndex(pointerId)
                    val x = e.getX(pointerIndex)
                    val y = e.getY(pointerIndex)
                    touchEvent.point = Vector2(x,height-y)
                    touchEvent
                }
             //   debug(currentList)
                engine.touchEmitter.onTouchesMoved(currentList)
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = e.actionIndex
                val x = e.getX(pointerIndex)
                val y = e.getY(pointerIndex)
                val pointerId = e.getPointerId(pointerIndex)
                val touchEvent = touchEvents[pointerId] ?: return false
                touchEvent.point = Vector2(x,height-y)
             //   debug(touchEvents.values.toList())
            //    engine.touchEmitter.onTouchesEnded(touchEvents.values.toList())}
                engine.touchEmitter.onTouchesEnded(listOf(touchEvent))
                touchEvents -= pointerId
            }
            MotionEvent.ACTION_CANCEL -> {
                touchEvents.clear()
                engine.touchEmitter.onTouchesCancelled()
            }

        }
        return true
    }
}