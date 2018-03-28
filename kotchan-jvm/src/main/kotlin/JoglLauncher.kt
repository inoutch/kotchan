import com.jogamp.newt.event.WindowAdapter
import com.jogamp.newt.event.WindowEvent
import com.jogamp.newt.opengl.GLWindow
import com.jogamp.opengl.*
import com.jogamp.opengl.util.FPSAnimator
import kotchan.Engine
import utility.type.Size

class JoglLauncher(title: String) : GLEventListener {
    private val glProfile = GLProfile.get(GLProfile.GL4ES3)
    private val glCaps = GLCapabilities(glProfile)
    private val glWindow = GLWindow.create(glCaps)
    private val animator = FPSAnimator(glWindow, 60)

    private lateinit var engine: Engine

    private var beforeMillis: Long = 0

    init {
        glWindow.title = title
        glWindow.isVisible = true
        glWindow.addWindowListener(object : WindowAdapter() {
            override fun windowDestroyNotify(e: WindowEvent) {
                System.exit(0)
            }
        })
        glWindow.addGLEventListener(this)
    }

    var windowSize: Size = Size(300.0f, 300.0f)

    fun run() {
        glWindow.setSize(windowSize.width.toInt() / 2, windowSize.height.toInt() / 2)
        animator.start()
    }

    override fun init(drawable: GLAutoDrawable?) {
        if (drawable == null || drawable.gl == null || !drawable.gl.isGL4ES3) {
            throw RuntimeException("jogl could not launch")
        }
        JoglProvider.gl = drawable.gl as GL4ES3
        engine = Engine()
    }

    override fun reshape(drawable: GLAutoDrawable?, x: Int, y: Int, width: Int, height: Int) {
        engine.reshape(x, y, width, height)
        beforeMillis = System.currentTimeMillis()
    }

    override fun display(drawable: GLAutoDrawable?) {
        val millisPerFrame = System.currentTimeMillis() - beforeMillis
        beforeMillis = System.currentTimeMillis()
        engine.render(millisPerFrame.toFloat() / 1000.0f)
    }

    override fun dispose(drawable: GLAutoDrawable?) {
    }
}