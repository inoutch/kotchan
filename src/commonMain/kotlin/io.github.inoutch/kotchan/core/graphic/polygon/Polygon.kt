package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.utility.type.*

open class Polygon(initMesh: Mesh, val material: Material) {
    var mesh = initMesh
        protected set

    var isPositionsDirty = true

    var isColorsDirty = true

    var isNormalsDirty = true

    var isTexcoordsDirty = true

    var visible = true
        set(value) {
            if (field != value)
                isPositionsDirty = true
            field = value
        }

    var position = Vector3()
        set(value) {
            if (field != value)
                isPositionsDirty = true
            field = value
        }

    var color = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        set(value) {
            if (field != value)
                isColorsDirty = true
            field = value
        }

    var scale = Vector3(1.0f, 1.0f, 1.0f)
        set(value) {
            if (field != value) {
                isPositionsDirty = true
            }
            field = value
        }

    open val positions = {
        if (isPositionsDirty) {
            val modelMatrix = transform()
            val pos = mesh.pos()
            positionArray = if (visible) {
                pos.map { Vector3(modelMatrix * Vector4(it, 1.0f)) }
            } else {
                pos.map { Vector3() }
            }.flatten()
            isPositionsDirty = false
        }
        positionArray
    }

    open val colors = {
        if (isColorsDirty) {
            val parent = this.parent
            val c = if (parent == null) color else color * parent.color
            colorArray = mesh.col()
                    .map { c }
                    .flatten()
            isColorsDirty = false
        }
        colorArray
    }

    open val texcoords = {
        if (isTexcoordsDirty) {
            texcoordArray = mesh.tex().flatten()
            isTexcoordsDirty = false
        }
        texcoordArray
    }

    open fun addChild(polygon: Polygon) {
        if (polygon.parent != null) {
            throw Error("$polygon: this view was already had a parent view")
        }
        polygon.parent = this
        children.add(polygon)
    }

    open fun addChildren(polygons: List<Polygon>) {
        polygons.forEach { addChild(it) }
    }

    open fun removeChild(polygon: Polygon) {
        if (polygon.parent != this) {
            throw Error("$polygon: doesn't have this view")
        }
        polygon.parent = null
        children.remove(polygon)
    }

    fun replaceMesh(mesh: Mesh) {
        this.mesh = mesh
        isPositionsDirty = true
        isTexcoordsDirty = true
        isColorsDirty = true
        isNormalsDirty = true
    }

    private var parent: Polygon? = null

    private val children = mutableListOf<Polygon>()

    private var positionArray = mesh.pos().flatten()

    private var texcoordArray = mesh.tex().flatten()

    private var colorArray = mesh.col().flatten()

    private var normalArray = mesh.nom().flatten()

    protected open fun transform(): Matrix4 {
        val parent = this.parent ?: return Matrix4.createTranslation(position) * Matrix4.createScale(scale)
        return Matrix4.createTranslation(position + parent.position) *
                Matrix4.createScale(scale * parent.scale)
    }
}
