package kotchan

import kotchan.view.Batch
import kotchan.view.View

class ViewController {
    private val batch = Batch()
    private var scene: View? = null

    fun runRender(scene: View) {
        this.scene = scene
    }

    fun render(delta: Float) {
        scene?.render(delta, batch)

        batch.flush(delta)
        batch.clear()
    }
}