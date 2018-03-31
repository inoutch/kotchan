package utility.type

import kotchan.Engine

class Mesh(
        val vertices: MutableList<Float>,
        val texcoords: MutableList<Float>,
        val colors: MutableList<Float>) {
    private val gl = Engine.getInstance().gl
    private var isDirtyVertices = false
    private var isDirtyTexcoords = false
    private var isDirtyColors = false
}