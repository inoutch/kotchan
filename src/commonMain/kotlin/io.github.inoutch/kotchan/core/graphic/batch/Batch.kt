package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon
import io.github.inoutch.kotchan.utility.Disposable

class Batch : Disposable {

    companion object {
        const val BATCH_BUFFER_SIZE = 10000
    }

    private val polygons = mutableMapOf<Material, BatchPolygonBundle>()

    private val invPolygonCache = mutableMapOf<Polygon, Material>()

    private val invPolygonBundleCache = mutableMapOf<Polygon, BatchBundle>()

    fun add(vararg polygons: Polygon) {
        polygons.forEach { add(it) }
    }

    fun add(polygons: List<Polygon>) {
        polygons.forEach { add(it) }
    }

    fun add(polygon: Polygon, autoAddChildren: Boolean = true) {
        if (invPolygonCache[polygon] != null) {
            return
        }

        if (autoAddChildren) {
            add(polygon.children)
        }

        val material = polygon.material ?: return
        if (polygon.mesh.size <= 0) {
            return
        }

        val batchPolygonBundle = polygons.getOrPut(material) {
            BatchPolygonBundle(
                    BatchBuffer(BATCH_BUFFER_SIZE * 3),
                    BatchBuffer(BATCH_BUFFER_SIZE * 4),
                    BatchBuffer(BATCH_BUFFER_SIZE * 2))
        }
        batchPolygonBundle.apply {
            val positionBufferData = positionBuffer.add(polygon.positions())
            val texcoordBufferData = texcoordBuffer.add(polygon.texcoords())
            val colorBufferData = colorBuffer.add(polygon.colors())
            val bundle = BatchBundle(polygon, positionBufferData, colorBufferData, texcoordBufferData)
            invPolygonBundleCache[polygon] = bundle
            polygons.add(bundle)
        }
        invPolygonCache[polygon] = material
    }

    fun remove(polygon: Polygon) {
        val material = invPolygonCache[polygon]
                ?: polygons.filterValues { values -> values.polygons.find { polygon == it.polygon } != null }
                        .keys.firstOrNull()
                ?: return
        val batchPolygonBundle = polygons[material] ?: return
        val bundle = invPolygonBundleCache[polygon]
                ?: batchPolygonBundle.polygons.find { polygon == it.polygon }
                ?: return

        batchPolygonBundle.positionBuffer.remove(bundle.positionBufferData)
        batchPolygonBundle.colorBuffer.remove(bundle.colorBufferData)
        batchPolygonBundle.texcoordBuffer.remove(bundle.texcoordBufferData)
        batchPolygonBundle.polygons.remove(bundle)
    }

    fun draw(delta: Float, camera: Camera) {
        polygons.forEach { pair ->
            val material = pair.key
            material.graphicsPipeline.bind()
            material.graphicsPipeline.shaderProgram.prepare(delta, camera.combine, material.textures)

            val polygonBundle = pair.value
            polygonBundle.positionBuffer.flush()
            polygonBundle.texcoordBuffer.flush()
            polygonBundle.colorBuffer.flush()

            polygonBundle.polygons.forEach { batchBundle ->
                batchBundle.polygon.positionChanges.forEach { partialChange ->
                    polygonBundle.positionBuffer.copy(batchBundle.positionBufferData, partialChange)
                }
                batchBundle.polygon.texcoordChanges.forEach { partialChange ->
                    polygonBundle.texcoordBuffer.copy(batchBundle.texcoordBufferData, partialChange)
                }
                batchBundle.polygon.colorChanges.forEach { partialChange ->
                    polygonBundle.colorBuffer.copy(batchBundle.colorBufferData, partialChange)
                }
//              batchBundle.polygon.normalChanges.forEach { partialChange ->
//                  polygonBundle.normalBuffer.copy(batchBundle.normalBufferData, partialChange)
//              }
            }

            polygonBundle.polygons.filter { it.polygon.isPositionsDirty }
                    .forEach { polygonBundle.positionBuffer.copy(it.positionBufferData, it.polygon.positions()) }
            polygonBundle.polygons.filter { it.polygon.isColorsDirty }
                    .forEach { polygonBundle.colorBuffer.copy(it.colorBufferData, it.polygon.colors()) }
            polygonBundle.polygons.filter { it.polygon.isTexcoordsDirty }
                    .forEach { polygonBundle.texcoordBuffer.copy(it.texcoordBufferData, it.polygon.texcoords()) }

            instance.graphicsApi.drawTriangles(polygonBundle)
        }
    }

    override fun dispose() {
        polygons.forEach { pair ->
            pair.value.apply {
                positionBuffer.dispose()
                colorBuffer.dispose()
                texcoordBuffer.dispose()
            }
        }
        polygons.clear()
        invPolygonCache.clear()
    }
}
