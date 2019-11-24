package io.github.inoutch.kotchan.core.controller.touch

import io.github.inoutch.kotchan.utility.type.Point

interface Touch {

    fun index(): Int

    fun point(): Point

    fun type(): TouchType
}
