package application

import interop.graphic.GLCamera
import interop.graphic.GLFilterType
import kotchan.scene.batch.Batch
import kotchan.scene.Scene
import kotchan.scene.drawable.Sprite
import kotchan.scene.drawable.Square
import kotchan.scene.shader.SimpleShaderProgram
import kotchan.tool.TexturePacker
import utility.math.Random
import utility.type.Size
import utility.type.Vector3

class AppScene : Scene() {
    private val ratio = 2
    private val camera = GLCamera.createOrthographic(0.0f, screenSize.width / ratio, 0.0f, screenSize.height / ratio, -1.0f, 1.0f)
    private val shaderProgram = SimpleShaderProgram()
    private val texture = textureManager.get(file.getResourcePath("textures/sample.png"))
    private val sprites = MutableList(500) {
        Square(Size(32.0f, 32.0f), texture).also {
            val x = Random.next((screenSize.width / ratio).toInt())
            val y = Random.next((screenSize.height / ratio).toInt())
            it.position = Vector3(x.toFloat(), y.toFloat(), 0.0f)
        }
    }
    private val spriteBatch = Batch().apply { sprites.forEach { add(it, shaderProgram) } }
    private var timer = 0
    private var sprite: Sprite? = null

    init {
        val fullpath = file.getResourcePath("textures/spritesheet.json")
        val dirpath = file.getResourcePath("textures")
        if (fullpath != null && dirpath != null) {
            TexturePacker.loadFile(dirpath, fullpath)?.let {
                val sprite = Sprite(it)
                spriteBatch.add(sprite, shaderProgram)
                sprite.setAtlas("RunRight01.png")
                sprite.texture.filterType = GLFilterType.Linear
                this.sprite = sprite
            }
        }
    }

    override fun render(delta: Float) {
        gl.clearColor(0.0f, 1.0f, 1.0f, 1.0f)
        gl.enableBlend()
        gl.activeTexture(0)
        gl.enableTexture()

        camera.update()
        spriteBatch.draw(delta, camera)

        // create and destroy
        if (timer % Random.next(5, 1) == 0) {
            sprites.firstOrNull()?.let {
                sprites.remove(it)
                spriteBatch.remove(it)
            }
        } else if (timer % Random.next(5, 1) == 0) {
            spriteBatch.add(Square(Size(32.0f, 32.0f)).also {
                it.texture = texture
                val x = Random.next((screenSize.width / ratio).toInt())
                val y = Random.next((screenSize.height / ratio).toInt())
                it.position = Vector3(x.toFloat(), y.toFloat(), 0.0f)
                sprites.add(it)
            }, shaderProgram)
        }

        // animation
        val names = listOf("RunRight01.png", "RunRight02.png", "RunRight03.png", "RunRight04.png")
        sprite?.setAtlas(names[(timer / 10) % names.size])
        timer += 1
    }

    override fun pause() {}

    override fun resume() {}
}