package io.github.inoutch.kotchan.core.platform

import android.opengl.GLSurfaceView
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable

class DispatcherGL(private val glSurfaceView: GLSurfaceView) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        glSurfaceView.queueEvent(block)
    }
}
