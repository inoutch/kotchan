package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.utility.type.*

open class Polygon(initMesh: Mesh, val material: Material?) {
    var mesh = initMesh
        protected set

    var isPositionsDirty = true

    var isColorsDirty = true

    var isNormalsDirty = true

    var isTexcoordsDirty = true

    var visible = true
        set(value) {
            if (field != value) {
                isPositionsDirty = true
                setDirtyPosition()
            }
            field = value
        }

    open var position = Vector3()
        set(value) {
            if (field != value) {
                isPositionsDirty = true
                setDirtyPosition()
            }
            field = value
        }

    open var color = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        set(value) {
            if (field != value) {
                isColorsDirty = true
                setDirtyColor()
            }
            field = value
        }

    open var scale = Vector3(1.0f, 1.0f, 1.0f)
        set(value) {
            if (field != value) {
                isPositionsDirty = true
                setDirtyPosition()
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

    open val positionChanges: List<PartialChange<FloatArray>>
        get() {
            val ret = privatePositionChanges.map { partials ->
                val positions = partials.value.map { Vector3(transform() * Vector4(it, 1.0f)) }.flatten()
                PartialChange(positions, partials.offset * 3)
            }
            privatePositionChanges.clear()
            return ret
        }

    open val colorChanges: List<PartialChange<FloatArray>>
        get() {
            val ret = privateColorChanges.toList()
            privateColorChanges.clear()
            return ret
        }

    open val texcoordChanges: List<PartialChange<FloatArray>>
        get() {
            val ret = privateTexcoordChanges.toList()
            privateTexcoordChanges.clear()
            return ret
        }

    open val normalChanges: List<PartialChange<FloatArray>>
        get() {
            val ret = privateNormalChanges.toList()
            privateNormalChanges.clear()
            return ret
        }

    open fun addChild(polygon: Polygon) {
        if (polygon.parent != null) {
            throw Error("$polygon: this view was already had a parent view")
        }
        polygon.parent = this
        privateChildren.add(polygon)
    }

    open fun addChildren(polygons: List<Polygon>) {
        polygons.forEach { addChild(it) }
    }

    open fun removeChild(polygon: Polygon) {
        if (polygon.parent != this) {
            throw Error("$polygon: doesn't have this view")
        }
        polygon.parent = null
        privateChildren.remove(polygon)
    }

    fun replaceMesh(mesh: Mesh) {
        this.mesh = mesh
        isPositionsDirty = true
        isTexcoordsDirty = true
        isColorsDirty = true
        isNormalsDirty = true
    }

    val children: List<Polygon>
        get() = privateChildren

    private var parent: Polygon? = null

    private val privateChildren = mutableListOf<Polygon>()

    private var positionArray = mesh.pos().flatten()

    private var texcoordArray = mesh.tex().flatten()

    private var colorArray = mesh.col().flatten()

    private var normalArray = mesh.nom().flatten()

    private val privatePositionChanges = mutableListOf<PartialChange<List<Vector3>>>()

    private val privateTexcoordChanges = mutableListOf<PartialChange<FloatArray>>()

    private val privateColorChanges = mutableListOf<PartialChange<FloatArray>>()

    private val privateNormalChanges = mutableListOf<PartialChange<FloatArray>>()

    protected open fun transform(): Matrix4 {
        val currentTransform = Matrix4.createTranslation(position) * Matrix4.createScale(scale)
        val parent = this.parent ?: return currentTransform
        return parent.childrenTransform() * currentTransform
    }

    protected open fun childrenTransform() = transform()

    fun updatePositions(positions: List<Vector3>, offset: Int) {
        privatePositionChanges.add(PartialChange(positions, offset))
        mesh.updatePositions(positions, offset)
    }

    fun updateTexcoords(texcoords: List<Vector2>, offset: Int) {
        privateTexcoordChanges.add(PartialChange(texcoords.flatten(), offset * 2))
        mesh.updateTexcoords(texcoords, offset)
    }

    fun updateColors(colors: List<Vector4>, offset: Int) {
        privateColorChanges.add(PartialChange(colors.flatten(), offset * 4))
        mesh.updateColors(colors, offset)
    }

    private fun setDirtyPosition() {
        isPositionsDirty = true
        privateChildren.forEach { it.setDirtyPosition() }
    }

    private fun setDirtyColor() {
        isColorsDirty = true
        privateChildren.forEach { it.setDirtyColor() }
    }
}
