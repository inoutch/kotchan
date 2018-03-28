package application

import kotchan.Engine
import kotchan.view.Batch
import kotchan.view.View
import kotchan.view.drawable.Square
import utility.type.Size

class AppDelegate : View() {
    private val square: Square = Square(Size(32.0f, 32.0f))

    override fun render(delta: Float, batch: Batch) {
        Engine.getInstance().gl.clearColor(0.0f, 1.0f, 1.0f, 1.0f)
        batch.add(square)
    }

    override fun pause() {}

    override fun resume() {}
}