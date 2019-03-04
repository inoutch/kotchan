//package io.github.inoutch.kotchan.core.graphic.view
//
//import io.github.inoutch.kotchan.utility.graphic.GLAttribLocation
//import io.github.inoutch.kotchan.core.graphic.camera.Camera
//import io.github.inoutch.kotchan.utility.graphic.GLTexture
//import io.github.inoutch.kotchan.utility.graphic.GLVBO
//import io.github.inoutch.kotchan.core.KotchanCore
//import io.github.inoutch.kotchan.core.graphic.shader.NoColorsShaderProgram
//import io.github.inoutch.kotchan.utility.type.*
//
//open class View(initMesh: Mesh, var texture: GLTexture = GLTexture.empty) : ViewBase() {
//
//    var mesh = initMesh
//        protected set
//
//    var isTexcoordsDirty = true
//        protected set
//
//    open val positions = {
//        if (isPositionsDirty) {
//            val modelMatrix = transform()
//            val pos = mesh.pos()
//            positionsBuffer = if (visible) {
//                pos.map { modelMatrix * Vector4(it, 1.0f) }.map { Vector3(it) }
//            } else {
//                pos.map { Vector3() }
//
//            }.flatten()
//            isPositionsDirty = false
//        }
//        positionsBuffer
//    }
//
//    open val colors = {
//        if (isColorsDirty) {
//            val parent = this.parent
//            val c = if (parent == null) color else color * parent.color
//            colorsBuffer = mesh.col()
//                    .map { c }
//                    .flatten()
//            isColorsDirty = false
//        }
//        colorsBuffer
//    }
//
//    open val texcoords = {
//        if (isTexcoordsDirty) {
//            texcoordsBuffer = mesh.tex().flatten()
//            isTexcoordsDirty = false
//        }
//        texcoordsBuffer
//    }
//
//    private val gl = KotchanCore.instance.gl
//
//    private var positionsBuffer = mesh.pos().flatten()
//
//    private var texcoordsBuffer = mesh.tex().flatten()
//
//    private var colorsBuffer = mesh.col().flatten()
//
//    private var vbo: GLVBO? = null
//
//    protected open fun transform(): Matrix4 {
//        val parent = this.parent
//                ?: return Matrix4.createTranslation(position) * Matrix4.createScale(scale)
//        return Matrix4.createTranslation(position + parent.position) *
//                Matrix4.createScale(scale * parent.scale)
//    }
//
//    fun bind() {
//        vbo = gl.createVBO(vertices())
//    }
//
//    fun replaceMesh(mesh: Mesh) {
//        this.mesh = mesh
//        isPositionsDirty = true
//        isTexcoordsDirty = true
//        isColorsDirty = true
//    }
//
//    open fun draw(delta: Float, shaderProgram: NoColorsShaderProgram, camera: Camera) {
//        if (!visible) {
//            return
//        }
//
//        val vbo = vbo ?: return
//        shaderProgram.use()
//        shaderProgram.modelMatrix4 = transform()
//        shaderProgram.prepare(delta, camera.combine)
//        texture.use()
//
//        gl.bindVBO(vbo.id)
//        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 5, 0)
//        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 5, 3)
//
//        gl.drawTriangleArrays(0, mesh.size)
//    }
//
//    override fun destroy() {
//        super.destroy()
//
//        vbo?.destroy()
//        texture.destroy()
//    }
//
//    private fun vertices(): FloatArray {
//        val vertices: MutableList<Float> = mutableListOf()
//        for (i in 0 until mesh.size) {
//            mesh.getVertex(i)?.let {
//                vertices.add(it.position.x)
//                vertices.add(it.position.y)
//                vertices.add(it.position.z)
//
//                vertices.add(it.texcoord.x)
//                vertices.add(it.texcoord.y)
//            }
//        }
//        return vertices.toFloatArray()
//    }
//}
