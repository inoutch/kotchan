package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.BufferInterface
import io.github.inoutch.kotchan.core.graphic.ChangeRange
import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.math.Matrix4F
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

open class Polygon(initMesh: Mesh) {
    // Base mesh
    var mesh = initMesh
        protected set

    var visible = true
        set(value) {
            if (field != value) {
                updatePositionAll()
            }
            field = value
        }

    open var position = Vector3F.Zero
        set(value) {
            if (field != value) {
                updatePositionAll()
            }
            field = value
        }

    open var color = Vector4F(1.0f, 1.0f, 1.0f, 1.0f)
        set(value) {
            if (field != value) {
                updateColorAll()
            }
            field = value
        }

    open var scale = Vector3F(1.0f, 1.0f, 1.0f)
        set(value) {
            if (field != value) {
                updatePositionAll()
            }
            field = value
        }

    val children: List<Polygon>
        get() = privateChildren

    protected val positionsChange = ChangeRange(mesh.pos().size)

    protected val colorsChange = ChangeRange(mesh.col().size)

    protected val texcoordsChange = ChangeRange(mesh.tex().size)

    protected val normalsChange = ChangeRange(mesh.nom().size)

    protected var parent: Polygon? = null

    private val privateChildren = mutableListOf<Polygon>()

    private val childVisible: Boolean
        get() = visible && (parent == null || parent?.childVisible == true)

    open fun addChild(polygon: Polygon) {
        checkNotNull(polygon.parent == null) {
            "$polygon: This view was already had a parent view"
        }
        polygon.parent = this
        privateChildren.add(polygon)
    }

    open fun addChildren(polygons: List<Polygon>) {
        polygons.forEach { addChild(it) }
    }

    open fun removeChild(polygon: Polygon) {
        check(polygon.parent == this) {
            "$polygon: Doesn't have this view"
        }
        polygon.parent = null
        privateChildren.remove(polygon)
    }

    fun updateMesh(mesh: Mesh) {
        this.mesh = mesh
        positionsChange.updateAll()
        colorsChange.updateAll()
        texcoordsChange.updateAll()
        normalsChange.updateAll()
    }

    fun updatePositions(positions: List<Vector3F>, offset: Int = 0) {
        mesh.updatePositions(positions, offset)
        positionsChange.update(offset, offset + positions.size)
    }

    fun updateTexcoords(texcoords: List<Vector2F>, offset: Int = 0) {
        mesh.updateTexcoords(texcoords, offset)
        texcoordsChange.update(offset, offset + texcoords.size)
    }

    fun updateColors(colors: List<Vector4F>, offset: Int = 0) {
        mesh.updateColors(colors, offset)
        colorsChange.update(offset, offset + texcoordsChange.size)
    }

    fun updateNormals(normals: List<Vector3F>, offset: Int = 0) {
        mesh.updateNormals(normals, offset)
        normalsChange.update(offset, offset + normals.size)
    }

    fun allPolygons(callback: (child: Polygon) -> Unit) {
        callback(this)
        if (children.isEmpty()) {
            return
        }
        var i = 0
        while (i < children.size) {
            children[i].allPolygons(callback)
            i++
        }
    }

    fun copyPositionsTo(target: BufferInterface<Float>, offset: Int = 0) {
        val change = positionsChange.change ?: return
        val modelMatrix = transform()
        val pos = mesh.pos()
        var i = change.first
        if (childVisible) {
            while (i < change.last) {
                val p = modelMatrix * Vector4F(pos[i], 1.0f)
                target.copy(offset + i * 3 + 0, p.x)
                target.copy(offset + i * 3 + 1, p.y)
                target.copy(offset + i * 3 + 2, p.z)
                i++
            }
        } else {
            while (i < change.last) {
                target.copy(offset + i * 3 + 0, 0.0f)
                target.copy(offset + i * 3 + 1, 0.0f)
                target.copy(offset + i * 3 + 2, 0.0f)
                i++
            }
        }
        target.range(offset + change.first * 3, offset + change.last * 3)
        positionsChange.reset()
    }

    fun copyColorsTo(target: BufferInterface<Float>, offset: Int = 0) {
        val change = colorsChange.change ?: return
        val parent = this.parent
        val nodeColor = if (parent == null) color else color * parent.color
        val col = mesh.col()
        var i = change.first
        while (i < change.last) {
            val c = col[i] * nodeColor
            target.copy(offset + i * 4 + 0, c.x)
            target.copy(offset + i * 4 + 1, c.y)
            target.copy(offset + i * 4 + 2, c.z)
            target.copy(offset + i * 4 + 3, c.w)
            i++
        }
        target.range(offset + change.first * 4, offset + change.last * 4)
        colorsChange.reset()
    }

    fun copyTexcoordsTo(target: BufferInterface<Float>, offset: Int = 0) {
        val change = texcoordsChange.change ?: return
        val tex = mesh.tex()
        var i = change.first
        while (i < change.last) {
            val t = tex[i]
            target.copy(offset + i * 2 + 0, t.x)
            target.copy(offset + i * 2 + 1, t.y)
            i++
        }
        target.range(offset + change.first * 2, offset + change.last * 2)
        texcoordsChange.reset()
    }

    fun copyNormalsTo(target: BufferInterface<Float>, offset: Int = 0) {
        val change = normalsChange.change ?: return
        val nom = mesh.nom()
        var i = change.first
        while (i < change.last) {
            val n = nom[i]
            target.copy(offset + i * 3 + 0, n.x)
            target.copy(offset + i * 3 + 1, n.y)
            target.copy(offset + i * 3 + 2, n.z)
            i++
        }
        target.range(offset + change.first * 3, offset + change.last * 3)
        normalsChange.reset()
    }

    protected fun updatePositionAll() {
        allPolygons { it.positionsChange.updateAll() }
    }

    protected fun updateColorAll() {
        allPolygons { it.colorsChange.updateAll() }
    }

    protected fun updateTexcoordAll() {
        allPolygons { it.texcoordsChange.updateAll() }
    }

    protected open fun transform(): Matrix4F {
        val currentTransform = Matrix4F.createTranslation(position) * Matrix4F.createScale(scale)
        val parent = this.parent ?: return currentTransform
        return parent.childrenTransform() * currentTransform
    }

    protected open fun childrenTransform() = transform()
}
