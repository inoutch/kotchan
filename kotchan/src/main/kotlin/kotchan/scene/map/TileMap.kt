package kotchan.scene.map

import interop.graphic.GLCamera
import kotchan.scene.shader.NoColorsShaderProgram

class TileMap(private val mapInfo: TileMapInfo) {
    private val shaderProgram = NoColorsShaderProgram()
    private val layers: List<TileLayer> = mapInfo.layersInfo.map { TileLayer(mapInfo, it) }

    fun draw(delta: Float, camera: GLCamera) {
        mapInfo.texture.use()
        shaderProgram.prepare(delta, camera)
        layers.forEach { it.draw() }
    }

    fun destroy() {
        layers.forEach { it.destroy() }
        shaderProgram.destroy()
    }
}