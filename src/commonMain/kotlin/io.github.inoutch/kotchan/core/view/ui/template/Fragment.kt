package io.github.inoutch.kotchan.core.view.ui.template

import io.github.inoutch.kotchan.utility.type.Vector4
import io.github.inoutch.kotchan.core.view.drawable.DrawableBase

data class Fragment(val type: TemplateAppendType, val drawables: MutableList<Pair<DrawableBase, Vector4>> = mutableListOf())