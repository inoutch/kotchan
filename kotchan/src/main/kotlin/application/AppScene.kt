package application

import interop.graphic.*
import kotchan.scene.batch.Batch
import kotchan.scene.Scene
import kotchan.scene.map.TileLayerInfo
import kotchan.scene.map.TileMap
import kotchan.scene.map.TileMapInfo
import kotchan.scene.shader.NoColorsShaderProgram
import kotchan.scene.shader.SimpleShaderProgram
import kotchan.tool.TexturePacker
import kotchan.ui.Button
import utility.math.Random
import utility.type.Vector2

class AppScene : Scene() {
    // common data
    private val ratio = 4.0f
    private val camera = GLCamera.createOrthographic(0.0f, screenSize.x / ratio, 0.0f, screenSize.y / ratio, -1.0f, 1.0f)
    private val shaderProgram1 = SimpleShaderProgram()
    private val shaderProgram2 = NoColorsShaderProgram()
    private val spriteBatch = Batch()
    private var timer = 0

    // sprites
    private var button: Button? = null

    private var tileMap: TileMap? = null

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

        // tilemap
        val tileMapTexturePath = file.getResourcePath("textures/debug.png")
        if (tileMapTexturePath != null) {
            val tileMapTexture = gl.loadTexture(tileMapTexturePath)
            if (tileMapTexture != null) {
                val tileMapInfo = TileMapInfo(
                        "hoge",
                        Vector2(16.0f, 16.0f),
                        tileMapTexture, listOf(TileLayerInfo(listOf(
                        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                        listOf(0, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                        listOf(0, 1, 2, 2, 4, 5, 6, 7, 8, 9, 10),
                        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                        listOf(0, 1, 2, 3, 4, 5, 8, 7, 8, 9, 10),
                        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 10),
                        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)))))
                tileMap = TileMap(tileMapInfo)
            }
        }

        gl.enableBlend()
        gl.activeTexture(0)
        gl.enableTexture()
    }

    override fun render(delta: Float) {
        gl.clearColor(0.0f, 1.0f, 1.0f, 1.0f)
        if (timer % 100 == 0) {
            tileMap?.layer(0)?.mapId(3, 4, Random.next(9))
            tileMap?.layer(0)?.mapId(4, 4, Random.next(9))
            tileMap?.layer(0)?.mapId(5, 4, Random.next(9))
            tileMap?.layer(0)?.mapId(3, 5, Random.next(9))
            tileMap?.layer(0)?.mapId(4, 5, Random.next(9))
            tileMap?.layer(0)?.mapId(5, 5, Random.next(9))
        }

        camera.update()
        button?.draw(delta, shaderProgram2, camera)
        //tileMap?.draw(delta, camera)
        timer++
    }

    override fun pause() {}

    override fun resume() {}
}