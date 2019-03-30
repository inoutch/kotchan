package io.github.inoutch.kotchan.core.graphic.ui

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon2D
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.Updatable

abstract class UI(private val batch: Batch) : Updatable, Disposable {

    private val touchables = mutableMapOf<Polygon2D, Touchable?>()

    var visible: Boolean = true
        set(value) {
            touchables.forEach {
                it.key.visible = value
                it.value?.touchListener?.enable = value
            }
            field = value
        }

    open fun <T> add(touchable: T, priority: Int = 0): T where T : Touchable, T : Polygon2D {
        instance.touchController.add(touchable.touchListener, priority)
        touchables[touchable] = touchable
        batch.add(touchable)
        return touchable
    }

    open fun <T : Polygon2D> add(polygon: T): T {
        touchables[polygon] = null
        batch.add(polygon)
        return polygon
    }

    open fun <T> remove(touchable: T) where T : Touchable, T : Polygon2D {
        touchables[touchable]?.let { instance.touchController.remove(it.touchListener) }
        touchables.remove(touchable)
        batch.remove(touchable)
    }

    override fun dispose() {
        touchables.forEach {
            batch.remove(it.key)
            it.value?.touchListener?.let { x -> instance.touchController.remove(x) }
        }
        touchables.clear()
    }

    override fun update(delta: Float) {
        touchables.keys.forEach { it.update(delta) }
    }
}
