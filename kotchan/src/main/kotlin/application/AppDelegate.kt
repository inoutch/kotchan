package application

import kotchan.Engine
import kotchan.view.batch.Batch
import kotchan.view.View
import kotchan.view.drawable.Square
import utility.type.Size

class AppDelegate : View() {
    private val square: Square = Square(Size(0.2f, 0.2f))
    private val spriteBatch = Batch().apply {
        add(square)
    }

    private val gl = Engine.getInstance().gl

    override fun render(delta: Float) {
        gl.clearColor(0.0f, 1.0f, 1.0f, 1.0f)
        spriteBatch.draw(delta)
    }

    override fun pause() {}

    override fun resume() {}
}