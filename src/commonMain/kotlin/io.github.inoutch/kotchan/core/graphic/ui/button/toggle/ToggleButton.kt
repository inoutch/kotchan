import io.github.inoutch.kotchan.core.graphic.ui.Touchable

interface ToggleButton : Touchable {

    var state: Boolean

    val onToggleOn: () -> Unit

    val onToggleOff: () -> Unit

    fun click() {
        state = !state
        if (state) {
            onToggleOn()
        } else {
            onToggleOff()
        }
    }
}
