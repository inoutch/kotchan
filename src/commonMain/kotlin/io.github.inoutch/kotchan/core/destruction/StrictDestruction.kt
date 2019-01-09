package io.github.inoutch.kotchan.core.destruction

abstract class StrictDestruction : Destruction {
    var isDestroyed: Boolean = false
        private set

    fun check() {
        if (isDestroyed) {
            throw AlreadyDestroyedError(this::class)
        }
    }

    override fun destroy() {
        check()
        isDestroyed = true
    }
}