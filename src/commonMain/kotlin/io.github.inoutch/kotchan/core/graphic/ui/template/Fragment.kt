package io.github.inoutch.kotchan.core.graphic.ui.template

import io.github.inoutch.kotchan.core.graphic.view.View2DBase
import io.github.inoutch.kotchan.utility.type.Vector4

data class Fragment(val type: TemplateAppendType, val views: MutableList<Pair<View2DBase, Vector4>> = mutableListOf())
