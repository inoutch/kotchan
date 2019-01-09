package io.github.inoutch.kotchan.core.destruction

abstract class SafeDestruction : Destruction {
    var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) {
            throw AlreadyDestroyedError(this::class)
        }
        isDestroyed = true
    }
}