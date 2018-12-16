package io.github.inoutch.kotchan.core.view.ui.template

import io.github.inoutch.kotchan.utility.type.Vector4
import kotchan.view.drawable.Drawable

data class Fragment(val type: TemplateAppendType, val drawables: MutableList<Pair<Drawable, Vector4>> = mutableListOf())