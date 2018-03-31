package kotchan.view

abstract class View {
    abstract fun render(delta: Float)
    abstract fun pause()
    abstract fun resume()
}