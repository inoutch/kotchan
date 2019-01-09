package io.github.inoutch.kotchan.core.model

import io.github.inoutch.kotchan.core.destruction.Destruction

interface Model : Destruction {

    fun update(delta: Float)
}