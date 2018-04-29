package kotchan.view.animator.animate

import utility.type.Vector3
import kotlin.math.pow

class SmoothUp(duration: Float,
               private val exponent: Int,
               private val position: Vector3,
               private val positionCallback: (position: Vector3) -> Unit,
               callback: () -> Unit) : Animate(duration, callback) {
    override fun update(delta: Float) {
        val x = 1.0f - (duration - elapsedTime) / duration
        positionCallback(position * x.pow(exponent))
    }

    override fun done() {
        positionCallback(position)
    }
}