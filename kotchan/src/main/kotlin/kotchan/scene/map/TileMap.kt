package kotchan.scene.map

import interop.graphic.GLCamera
import kotchan.scene.shader.NoColorsShaderProgram

class TileMap(private val mapInfo: TileMapInfo) {
    private val shaderProgram = NoColorsShaderProgram()
    private val layers: List<TileLayer> = mapInfo.layersInfo.map { TileLayer(mapInfo, it) }

    fun draw(delta: Float, camera: GLCamera, layerRange: IntRange = IntRange(0, layers.size - 1)) {
        mapInfo.texture.use()
        shaderProgram.prepare(delta, camera)
        for (i in layerRange) {
            val l = layer(i) ?: continue
            if (l.visible) {
                l.draw()
            }
        }
    }

    fun layer(index: Int) = layers.getOrNull(index)

    fun destroy() {
        layers.forEach { it.destroy() }
        shaderProgram.destroy()
    }
}