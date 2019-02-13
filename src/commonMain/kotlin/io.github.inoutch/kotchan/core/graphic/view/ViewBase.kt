package io.github.inoutch.kotchan.core.graphic.view

import io.github.inoutch.kotchan.core.destruction.StrictDestruction
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

abstract class ViewBase : StrictDestruction() {

    var isPositionsDirty = true
        protected set

    var isColorsDirty = true
        protected set

    var visible = true
        set(value) {
            if (field != value)
                isPositionsDirty = true
            field = value
        }

    var position = Vector3()
        set(value) {
            if (field != value)
                isPositionsDirty = true
            field = value
        }

    var color = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        set(value) {
            if (field != value)
                isColorsDirty = true
            field = value
        }

    var scale = Vector3(1.0f, 1.0f, 1.0f)
        set(value) {
            if (field != value) {
                isPositionsDirty = true
            }
            field = value
        }

    val viewChildren: List<View>
        get() = children.toList()

    protected var parent: ViewBase? = null

    protected val children = mutableListOf<View>()

    open fun addChild(view: View) {
        if (view.parent != null) {
            throw Error("$view: this view was already had a parent view")
        }
        view.parent = this
        children.add(view)
    }

    open fun addChildren(views: List<View>) {
        views.forEach { addChild(it) }
    }

    open fun removeChild(view: View) {
        if (view.parent != this) {
            throw Error("$view: doesn't have this view")
        }
        view.parent = null
        children.remove(view)
    }
}
