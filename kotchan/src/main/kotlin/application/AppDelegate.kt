package application

import interop.graphic.GLCamera
import kotchan.view.batch.Batch
import kotchan.view.View
import kotchan.view.drawable.Square
import kotchan.view.shader.SimpleShaderProgram
import utility.math.Random
import utility.type.Size
import utility.type.Vector3

class AppDelegate : View() {
    private val ratio = 2
    private val camera = GLCamera.createOrthographic(0.0f, screenSize.width / ratio, 0.0f, screenSize.height / ratio, -1.0f, 1.0f)
    private val shaderProgram = SimpleShaderProgram()
    private val texture = textureManager.get(file.getResourcePath("textures/sample.png"))
    private val sprites = MutableList(100) {
        Square(Size(32.0f, 32.0f)).also {
            it.texture = texture
            val x = Random.next((screenSize.width / ratio).toInt())
            val y = Random.next((screenSize.height / ratio).toInt())
            it.position = Vector3(x.toFloat(), y.toFloat(), 0.0f)
        }
    }
    private val spriteBatch = Batch().apply { sprites.forEach { add(it, shaderProgram) } }

    override fun render(delta: Float) {
        gl.clearColor(0.0f, 1.0f, 1.0f, 1.0f)
        gl.enableBlend()
        gl.activeTexture(0)
        gl.enableTexture()

        camera.update()
        spriteBatch.draw(delta, camera)
    }

    override fun pause() {}

    override fun resume() {}
}