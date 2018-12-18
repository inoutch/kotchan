package io.github.inoutch.jvm

import com.jogamp.newt.event.MouseEvent
import com.jogamp.newt.event.MouseListener
import com.jogamp.newt.event.WindowAdapter
import com.jogamp.newt.event.WindowEvent
import com.jogamp.newt.opengl.GLWindow
import com.jogamp.opengl.*
import com.jogamp.opengl.util.FPSAnimator
import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.core.controller.touch.TouchEvent
import io.github.inoutch.kotchan.utility.type.Point

class JoglLauncher(private val config: KotchanEngine.Config) : GLEventListener, MouseListener {

    private lateinit var core: KotchanCore

    private val glProfile = GLProfile.get(GLProfile.GL4ES3)

    private val glCaps = GLCapabilities(glProfile)

    private val glWindow = GLWindow.create(glCaps)

    private val animator = FPSAnimator(glWindow, 60)

    private var singleTouchEvent: TouchEvent? = null

    init {
        glWindow.title = config.appName
        glWindow.isVisible = true
        glWindow.addWindowListener(object : WindowAdapter() {
            override fun windowDestroyNotify(e: WindowEvent) {
                System.exit(0)
            }
        })
        glWindow.addGLEventListener(this)
        glWindow.addMouseListener(this)
    }

    fun run() {
        // should be 1/2
        glWindow.setSize(config.windowSize.x / 2, config.windowSize.y / 2)
        glWindow.isResizable = false
        animator.start()
    }

    override fun init(drawable: GLAutoDrawable) {
        if (drawable.gl == null || !drawable.gl.isGL4ES3) {
            throw RuntimeException("jogl could not launch")
        }
        JoglProvider.gl = drawable.gl as GL4ES3
        JoglProvider.gl?.glClear(GL4ES3.GL_COLOR_BUFFER_BIT)
        JoglProvider.gl?.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        core = KotchanCore(config)
        core.init()
    }

    override fun reshape(drawable: GLAutoDrawable?, x: Int, y: Int, width: Int, height: Int) {
        core.reshape(x, y, width, height)
    }

    override fun display(drawable: GLAutoDrawable?) {
        core.draw()
    }

    override fun dispose(drawable: GLAutoDrawable?) {
    }

    override fun mousePressed(e: MouseEvent) {
        val core = this.core
        singleTouchEvent = TouchEvent(Point(e.x, glWindow.surfaceHeight - e.y))
        singleTouchEvent?.let { core.touchEmitter.onTouchesBegan(listOf(it)) }
    }

    override fun mouseReleased(e: MouseEvent) {
        singleTouchEvent?.point = Point(e.x, glWindow.surfaceHeight - e.y)
        singleTouchEvent?.let { core.touchEmitter.onTouchesEnded(listOf(it)) }
    }

    override fun mouseDragged(e: MouseEvent) {
        singleTouchEvent?.point = Point(e.x, glWindow.surfaceHeight - e.y)
        singleTouchEvent?.let { core.touchEmitter.onTouchesMoved(listOf(it)) }
    }

    override fun mouseClicked(e: MouseEvent?) {}

    override fun mouseMoved(e: MouseEvent?) {}

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}

    override fun mouseWheelMoved(e: MouseEvent?) {}
}