package io.github.inoutch.kotchan.core.graphic.ui.template

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.destruction.StrictDestruction
import io.github.inoutch.kotchan.core.graphic.view.View2DBase
import io.github.inoutch.kotchan.utility.type.Rect
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

class Template(val rect: Rect = Rect(Vector2(), KotchanCore.instance.screenSize.toVector2())) : StrictDestruction() {
    companion object {
        fun createMargin(left: Float, right: Float, top: Float, bottom: Float) = Vector4(left, right, top, bottom)
    }

    var visible: Boolean = true
        set(value) {
            fragments.forEach { fragment -> fragment.value.views.forEach { it.first.visible = value } }
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

    fun add(type: TemplateType, appendType: TemplateAppendType, margin: Float, view: View2DBase) {
        add(type, appendType, Vector4(margin, margin, margin, margin), view)
    }

    fun add(type: TemplateType,
            appendType: TemplateAppendType,
            margin: Vector4,
            view: View2DBase) {
        val pair = this.fragments
                .getOrPut(type) { Fragment(appendType) }
        pair.views.add(Pair(view, margin))
    }

    fun clear() {
        fragments.clear()
    }

    fun updatePositions(offset: Vector2 = Vector2.Zero) {
        fragments.forEach { fragment ->
            val type = switchType(fragment.key)
            // calculation of overall views size
            val h = if (fragment.value.type == TemplateAppendType.Row) {
                fragment.value.views.sumByDouble { (it.first.size.y + it.second.z + it.second.w).toDouble() }.toFloat()
            } else 0.0f
            val w = if (fragment.value.type == TemplateAppendType.Column) {
                fragment.value.views.sumByDouble { (it.first.size.x + it.second.x + it.second.y).toDouble() }.toFloat()
            } else 0.0f

            val x = rect.origin.x + (rect.size.x - w) * h(type.first)
            val y = rect.origin.y + (rect.size.y - h) * v(type.second)
            val anchorPoint = Vector2(h(type.first), v(type.second))
            var position = Vector2(x, y)
            // calculation position
            fragment.value.views.forEach { pair ->
                val drawable = pair.first
                val margin = pair.second
                position += when (fragment.value.type) {
                    TemplateAppendType.Row -> Vector2(0.0f, margin.w + drawable.size.y * anchorPoint.y)
                    TemplateAppendType.Column -> Vector2(margin.x + drawable.size.x * anchorPoint.x, 0.0f)
                }
                val drawableOffset = when (fragment.value.type) {
                    TemplateAppendType.Row -> Vector2(margin.x - (margin.x + margin.y) * anchorPoint.x, 0.0f)
                    TemplateAppendType.Column -> Vector2(0.0f, margin.w - drawable.size.y * anchorPoint.y)
                }
                pair.first.position = Vector3(position + drawableOffset + offset, 0.0f)
                position += when (fragment.value.type) {
                    TemplateAppendType.Row -> Vector2(0.0f, margin.y + drawable.size.y * (1.0f - anchorPoint.y))
                    TemplateAppendType.Column -> Vector2(margin.z + drawable.size.x * (1.0f - anchorPoint.x), 0.0f)
                }
                pair.first.anchorPoint = anchorPoint
            }
        }
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
