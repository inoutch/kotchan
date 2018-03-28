package utility.type

import interop.graphic.GLVBO
import kotchan.Engine

class Mesh(val vertices: MutableList<Float>, val texcoords: MutableList<Float>, val colors: MutableList<Float>) {
    private val gl = Engine.getInstance().gl
    private var isDirtyVertices = false
    private var isDirtyTexcoords = false
    private var isDirtyColors = false
    private val vboVertices = gl.createVBO(vertices)
    private val vboTexcoords: GLVBO = gl.createVBO(texcoords)
    private val vboColors: GLVBO = gl.createVBO(colors)
}