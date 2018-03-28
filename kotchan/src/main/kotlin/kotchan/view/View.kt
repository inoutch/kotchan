package kotchan.view

import kotchan.view.Batch

abstract class View {
    abstract fun render(delta: Float, batch: Batch)
    abstract fun pause()
    abstract fun resume()
}