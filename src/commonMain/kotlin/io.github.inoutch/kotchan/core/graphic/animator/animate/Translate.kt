package io.github.inoutch.kotchan.core.graphic.animator.animate

import io.github.inoutch.kotchan.utility.type.Vector3

class Translate(
        duration: Float,
        private val position: Vector3,
        private val positionCallback: (position: Vector3) -> Unit,
        callback: () -> Unit) : Animate(duration, callback) {

    override fun update(delta: Float) {
        positionCallback(position * (elapsedTime / duration))
    }

    override fun done() {
        positionCallback(position)
    }
}
