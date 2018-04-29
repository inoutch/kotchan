package kotchan.view.animator.animate

class FadeOut(
        duration: Float,
        private val colorCallback: (alpha: Float) -> Unit,
        callback: () -> Unit) : Animate(duration, callback) {
    override fun update(delta: Float) {
        colorCallback(elapsedTime / duration)
    }

    override fun done() {
        colorCallback(1.0f)
    }
}