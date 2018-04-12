package application

import interop.graphic.*
import kotchan.scene.batch.Batch
import kotchan.scene.Scene
import kotchan.scene.shader.SimpleShaderProgram
import kotchan.tool.TexturePacker
import kotchan.ui.Button

class AppScene : Scene() {
    // common data
    private val ratio = 1.0f
    private val camera = GLCamera.createOrthographic(0.0f, screenSize.x / ratio, 0.0f, screenSize.y / ratio, -1.0f, 1.0f)
    private val shaderProgram = SimpleShaderProgram()
    private val spriteBatch = Batch()

    // sprites
    private var button: Button? = null

    init {
        val fullPath = file.getResourcePath("textures/spritesheet.json") ?: ""
        val dirPath = file.getResourcePath("textures") ?: ""
        TexturePacker.loadFile(dirPath, fullPath)?.let {
            button = Button(it, "RunRight01.png", "RunRight02.png", camera) {
                println("押された！")
                true
            }.also {
                it.bind()
                //spriteBatch.add(it, shaderProgram)
                touchController.add(it.touchable)
            }
        }

        gl.enableBlend()
        gl.activeTexture(0)
        gl.enableTexture()
    }

    override fun render(delta: Float) {
        gl.clearColor(0.0f, 1.0f, 1.0f, 1.0f)

        camera.update()
        button?.draw(delta, shaderProgram, camera)
    }

    override fun pause() {}

    override fun resume() {}
}