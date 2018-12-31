package io.github.inoutch.kotchan.core.view.drawable

import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

abstract class DrawableGroup : DrawableBase {
    abstract val nodes: List<Node>

    override var size = Vector2()
        protected set

    data class Node(val drawable: Drawable, val offset: Vector3)

    override var position = Vector3()
        set(value) {
            if (field != value)
                updatePosition(value, anchorPoint)
            field = value
        }

    var color = Vector4()
        set(value) {
            if (field != value)
                nodes.forEach { it.drawable.color = value }
            field = value
        }

    override var anchorPoint = Vector2(0.5f, 0.5f)
        set(value) {
            if (field != value)
                updatePosition(position, value)
            field = value
        }

    private fun updatePosition(position: Vector3, anchorPoint: Vector2) {
        nodes.forEach {
            it.drawable.position = position + it.offset + Vector3(size * anchorPoint * -1.0f, 0.0f)
        }
    }
}