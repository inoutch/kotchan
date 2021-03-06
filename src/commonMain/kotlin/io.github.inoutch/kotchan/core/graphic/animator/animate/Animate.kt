package io.github.inoutch.kotchan.core.graphic.animator.animate

abstract class Animate(val duration: Float, val callback: () -> Unit) {

    var elapsedTime: Float = 0.0f

    abstract fun update(delta: Float)

    abstract fun done()
}
