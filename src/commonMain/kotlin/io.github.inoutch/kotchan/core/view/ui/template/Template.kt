package io.github.inoutch.kotchan.core.view.ui.template

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.destruction.StrictDestruction
import io.github.inoutch.kotchan.core.model.Model
import io.github.inoutch.kotchan.utility.type.Rect
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4
import io.github.inoutch.kotchan.core.view.drawable.Drawable
import io.github.inoutch.kotchan.core.view.drawable.DrawableBase

class Template(val rect: Rect = Rect(Vector2(), KotchanCore.instance.screenSize.toVector2())) : StrictDestruction(), Model {
    companion object {
        fun createMargin(left: Float, right: Float, top: Float, bottom: Float) = Vector4(left, right, top, bottom)
    }

    var visible: Boolean = false
        set(value) {
            fragments.forEach { fragment -> fragment.value.drawables.forEach { visible = value } }
            field = value
        }

    private enum class HorizontalType {
        Left,
        Center,
        Right,
    }

    private enum class VerticalType {
        Top,
        Middle,
        Bottom,
    }

    private val fragments: MutableMap<TemplateType, Fragment> = mutableMapOf()

    fun add(type: TemplateType, appendType: TemplateAppendType, margin: Float, drawable: DrawableBase) {
        add(type, appendType, Vector4(margin, margin, margin, margin), drawable)
    }

    fun add(type: TemplateType,
            appendType: TemplateAppendType,
            margin: Vector4,
            drawable: DrawableBase) {
        val pair = this.fragments
                .getOrPut(type) { Fragment(appendType) }
        pair.drawables.add(Pair(drawable, margin))
    }

    fun clear() {
        fragments.clear()
    }

    fun updatePositions() {
        fragments.forEach { fragment ->
            val type = switchType(fragment.key)
            // calculation of overall drawables size
            val h = if (fragment.value.type == TemplateAppendType.Row) {
                fragment.value.drawables.sumByDouble { (it.first.size.y + it.second.z + it.second.w).toDouble() }.toFloat()
            } else 0.0f
            val w = if (fragment.value.type == TemplateAppendType.Column) {
                fragment.value.drawables.sumByDouble { (it.first.size.x + it.second.x + it.second.y).toDouble() }.toFloat()
            } else 0.0f

            val x = rect.origin.x + (rect.size.x - w) * h(type.first)
            val y = rect.origin.y + (rect.size.y - h) * v(type.second)
            val anchorPoint = Vector2(h(type.first), v(type.second))
            var position = Vector2(x, y)
            // calculation position
            fragment.value.drawables.forEach { pair ->
                val drawable = pair.first
                val margin = pair.second
                position += when (fragment.value.type) {
                    TemplateAppendType.Row -> Vector2(0.0f, margin.w + drawable.size.y * anchorPoint.y)
                    TemplateAppendType.Column -> Vector2(margin.x + drawable.size.x * anchorPoint.x, 0.0f)
                }
                val offset = when (fragment.value.type) {
                    TemplateAppendType.Row -> Vector2(margin.x - (margin.x + margin.y) * anchorPoint.x, 0.0f)
                    TemplateAppendType.Column -> Vector2(0.0f, margin.w - drawable.size.y * anchorPoint.y)
                }
                pair.first.position = Vector3(position + offset, 0.0f)
                position += when (fragment.value.type) {
                    TemplateAppendType.Row -> Vector2(0.0f, margin.y + drawable.size.y * (1.0f - anchorPoint.y))
                    TemplateAppendType.Column -> Vector2(margin.z + drawable.size.x * (1.0f - anchorPoint.x), 0.0f)
                }
                pair.first.anchorPoint = anchorPoint
            }
        }
    }

    override fun update(delta: Float) {
        fragments.values.forEach { fragment -> fragment.drawables.forEach { it.first.update(delta) } }
    }

    override fun destroy() {
        super.destroy()
        fragments.values.forEach { fragment -> fragment.drawables.forEach { it.first.destroy() } }
    }

    private fun h(type: HorizontalType) = when (type) {
        HorizontalType.Left -> 0.0f
        HorizontalType.Center -> 0.5f
        HorizontalType.Right -> 1.0f
    }

    private fun v(type: VerticalType) = when (type) {
        VerticalType.Top -> 1.0f
        VerticalType.Middle -> 0.5f
        VerticalType.Bottom -> 0.0f
    }

    private fun switchType(type: TemplateType) = when (type) {
        TemplateType.TopLeft -> Pair(HorizontalType.Left, VerticalType.Top)
        TemplateType.TopCenter -> Pair(HorizontalType.Center, VerticalType.Top)
        TemplateType.TopRight -> Pair(HorizontalType.Right, VerticalType.Top)
        TemplateType.MiddleLeft -> Pair(HorizontalType.Left, VerticalType.Middle)
        TemplateType.MiddleCenter -> Pair(HorizontalType.Center, VerticalType.Middle)
        TemplateType.MiddleRight -> Pair(HorizontalType.Right, VerticalType.Middle)
        TemplateType.BottomLeft -> Pair(HorizontalType.Left, VerticalType.Bottom)
        TemplateType.BottomCenter -> Pair(HorizontalType.Center, VerticalType.Bottom)
        TemplateType.BottomRight -> Pair(HorizontalType.Right, VerticalType.Bottom)
    }
}