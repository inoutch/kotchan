//package io.github.inoutch.kotchan.core.graphic.ui.button.toggle
//
//import io.github.inoutch.kotchan.core.model.Model
//
//interface ToggleButton : Model {
//
//    var state: Boolean
//
//    val onToggleOn: () -> Unit
//
//    val onToggleOff: () -> Unit
//
//    fun click() {
//        state = !state
//        if (state) {
//            onToggleOn()
//        } else {
//            onToggleOff()
//        }
//    }
//}