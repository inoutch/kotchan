package kotchan.scene.batch

class BatchBufferData(var start: Int, var vertices: FloatArray) {
    fun end() = start + vertices.size
}