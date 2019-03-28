package io.github.inoutch.kotchan.core.graphic.ui

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon2D
import io.github.inoutch.kotchan.utility.Disposable

abstract class UI(private val batch: Batch) : Disposable {

    private val touchables = mutableMapOf<Polygon2D, Touchable>()

    var visible: Boolean = true
        set(value) {
            touchables.forEach {
                it.key.visible = value
                it.value.touchListener.enable = value
            }
            field = value
        }

    open fun <T> add(touchable: T): T where T : Touchable, T : Polygon2D {
        instance.touchController.add(touchable.touchListener)
        touchables[touchable] = touchable
        batch.add(touchable)
        return touchable
    }

    open fun <T> remove(touchable: T) where T : Touchable, T : Polygon2D {
        touchables[touchable]?.let { instance.touchController.remove(it.touchListener) }
        touchables.remove(touchable)
        batch.remove(touchable)
    }

    override fun dispose() {
        touchables.forEach {
            batch.remove(it.key)
            instance.touchController.remove(it.value.touchListener)
        }
        touchables.clear()
    }
}
