package io.github.inoutch.kotchan.core.graphic.template

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon2D
import io.github.inoutch.kotchan.utility.type.Rect
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

class Template(val rect: Rect = Rect(Vector2.Zero, instance.screenSize.toVector2())) {
    companion object {
        fun createMargin(top: Float, right: Float, bottom: Float, left: Float) = Vector4(top, right, bottom, left)
    }

    var visible: Boolean = true
        set(value) {
            polygons.forEach { it.visible = value }
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

    private val fragments: MutableMap<TemplateType, MutableMap<TemplateAppendType, MutableList<Fragment>>> = mutableMapOf()

    val polygons: List<Polygon>
        get() = fragments.map { x -> x.value.map { y -> y.value }.flatten() }.flatten().map { it.polygon }

    fun add(type: TemplateType, appendType: TemplateAppendType, margin: Float, polygons: List<Polygon2D>) {
        add(type, appendType, Vector4(margin, margin, margin, margin), polygons)
    }

    fun add(type: TemplateType,
            appendType: TemplateAppendType,
            margin: Vector4,
            polygons: List<Polygon2D>) {
        add(type, appendType, polygons.map { Fragment(it, margin) })
    }

    fun add(type: TemplateType,
            appendType: TemplateAppendType,
            topBottomMargin: Vector2,
            leftRightMargin: Vector2,
            polygons: List<Polygon2D>) {
        add(type, appendType, polygons.map {
            Fragment(it, Vector4(
                    topBottomMargin.x, leftRightMargin.y, topBottomMargin.y, leftRightMargin.x))
        })
    }

    fun add(type: TemplateType,
            appendType: TemplateAppendType,
            topBottomMargin: Float,
            leftRightMargin: Float,
            polygons: List<Polygon2D>) {
        add(type,
                appendType,
                Vector2(topBottomMargin, topBottomMargin),
                Vector2(leftRightMargin, leftRightMargin),
                polygons)
    }

    fun add(type: TemplateType,
            appendType: TemplateAppendType,
            fragments: List<Fragment>) {
        this.fragments.getOrPut(type) { mutableMapOf() }
                .getOrPut(appendType) { mutableListOf() }
                .addAll(fragments)
    }

    fun clear() {
        fragments.clear()
    }

    fun updatePositions() {
        fragments.forEach { x ->
            val templateType = x.key
            x.value.forEach { y ->
                val templateAppendType = y.key
                val width = if (templateAppendType == TemplateAppendType.Column) {
                    y.value.sumByDouble { it.polygon.size.x.toDouble() + it.margin.toMarginWidth() }.toFloat()
                } else {
                    y.value.max { it.polygon.size.x + it.margin.toMarginWidth() }
                }
                val height = if (templateAppendType == TemplateAppendType.Row) {
                    y.value.sumByDouble { it.polygon.size.y.toDouble() + it.margin.toMarginHeight() }.toFloat()
                } else {
                    y.value.max { it.polygon.size.y + it.margin.toMarginHeight() }
                }
                val anchorPoint = switchType(templateType).let { Vector2(h(it.first), v(it.second)) }
                val basePosition = Vector2(
                        rect.size.x * anchorPoint.x - width * anchorPoint.x,
                        rect.size.y * anchorPoint.y - height * anchorPoint.y)
                var p = basePosition
                for (fragment in y.value) {
                    val polygonSize = fragment.polygon.size
                    val marginWidth = fragment.margin.toMarginWidth()
                    val marginHeight = fragment.margin.toMarginHeight()
                    val offset = when (templateAppendType) {
                        TemplateAppendType.Row -> Vector2((width - polygonSize.x - marginWidth) / 2.0f, 0.0f)
                        TemplateAppendType.Column -> Vector2(0.0f, (height - polygonSize.y - marginHeight) / 2.0f)
                    }
                    fragment.polygon.position =
                            Vector3(p + Vector2(fragment.margin.w, fragment.margin.z) + offset, 0.0f)
                    fragment.polygon.anchorPoint = Vector2.Zero
                    p += when (templateAppendType) {
                        TemplateAppendType.Row -> Vector2(0.0f, fragment.polygon.size.y + marginHeight)
                        TemplateAppendType.Column -> Vector2(fragment.polygon.size.x + marginWidth, 0.0f)
                    }
                }
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

fun Vector4.toMarginWidth() = y + w

fun Vector4.toMarginHeight() = x + z

fun <T> List<T>.max(callback: (item: T) -> Float): Float {
    var x = Float.MIN_VALUE
    for (item in this) {
        val y = callback(item)
        if (x < y) {
            x = y
        }
    }
    return x
}
