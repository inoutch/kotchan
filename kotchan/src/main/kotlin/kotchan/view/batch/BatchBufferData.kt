package kotchan.view.batch

data class BatchBufferData(val start: Int, var vertices: List<Float>) {
    fun end() = start + vertices.size
}