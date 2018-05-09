import application.AppConfig
import com.jogamp.newt.event.MouseEvent
import com.jogamp.newt.event.MouseListener
import com.jogamp.newt.event.WindowAdapter
import com.jogamp.newt.event.WindowEvent
import com.jogamp.newt.opengl.GLWindow
import com.jogamp.opengl.*
import com.jogamp.opengl.util.FPSAnimator
import kotchan.Engine
import kotchan.controller.touch.TouchEvent
import utility.type.Vector2

class JoglLauncher(title: String) : GLEventListener, MouseListener {
    private val glProfile = GLProfile.get(GLProfile.GL4ES3)
    private val glCaps = GLCapabilities(glProfile)
    private val glWindow = GLWindow.create(glCaps)
    private val animator = FPSAnimator(glWindow, 60)

    private var engine: Engine? = null
    private var singleTouchEvent: TouchEvent? = null

    init {
        glWindow.title = title
        glWindow.isVisible = true
        glWindow.addWindowListener(object : WindowAdapter() {
            override fun windowDestroyNotify(e: WindowEvent) {
                System.exit(0)
            }
        })
        glWindow.addGLEventListener(this)
        glWindow.addMouseListener(this)
    }

    private val windowSize = AppConfig.WINDOW_SIZE
    private var screenSize = AppConfig.SCREEN_SIZE

    fun run() {
        // should be 1/2
        glWindow.setSize(windowSize.x.toInt() / 2, windowSize.y.toInt() / 2)
        glWindow.isResizable = false
        animator.start()
    }

    override fun init(drawable: GLAutoDrawable?) {
        if (drawable == null || drawable.gl == null || !drawable.gl.isGL4ES3) {
            throw RuntimeException("jogl could not launch")
        }
        JoglProvider.gl = drawable.gl as GL4ES3
        JoglProvider.gl?.glClear(GL4ES3.GL_COLOR_BUFFER_BIT)
        JoglProvider.gl?.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        engine = Engine()
        engine?.init(windowSize, screenSize)
    }

    override fun reshape(drawable: GLAutoDrawable?, x: Int, y: Int, width: Int, height: Int) {
        engine?.reshape(x, y, width, height)
    }

    override fun display(drawable: GLAutoDrawable?) {
        engine?.draw()
    }

    override fun dispose(drawable: GLAutoDrawable?) {
    }

    override fun mousePressed(e: MouseEvent?) {
        val engine = this.engine
        if (e == null || engine == null) {
            return
        }
        val x = e.x.toFloat()
        val y = (glWindow.surfaceHeight - e.y).toFloat()
        singleTouchEvent = TouchEvent(Vector2(x, y))
        singleTouchEvent?.let { engine.touchEmitter.onTouchesBegan(listOf(it)) }
    }

    override fun mouseReleased(e: MouseEvent?) {
        val engine = this.engine
        if (e == null || engine == null) {
            return
        }
        val x = e.x.toFloat()
        val y = (glWindow.surfaceHeight - e.y).toFloat()
        singleTouchEvent?.point = Vector2(x, y)
        singleTouchEvent?.let { engine.touchEmitter.onTouchesEnded(listOf(it)) }
    }

    override fun mouseDragged(e: MouseEvent?) {
        val engine = this.engine
        if (e == null || engine == null) {
            return
        }
        val x = e.x.toFloat()
        val y = (glWindow.surfaceHeight - e.y).toFloat()
        singleTouchEvent?.point = Vector2(x, y)
        singleTouchEvent?.let { engine.touchEmitter.onTouchesMoved(listOf(it)) }
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mouseMoved(e: MouseEvent?) {}

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}

    override fun mouseWheelMoved(e: MouseEvent?) {}
}