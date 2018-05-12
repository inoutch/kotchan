package kotchan.controller.touch.listener

import kotchan.view.camera.Camera
import kotchan.controller.touch.TouchType
import utility.type.Vector2

class ButtonTouchListener(
        private val setNormalView: () -> Unit,
        private val setPressedView: () -> Unit,
        camera: Camera, private val onClick: () -> Unit) :
        TouchListener(camera) {
    // this size is applied from normal texture atlas
    private var isBegan = false

    override fun callback(index: Int, point: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean {
        if (index != 0) {
            return true
        }
        if (type == TouchType.Began && check && chain) {
            setPressedView()
            isBegan = true
        }
        if (type == TouchType.Ended) {
            if (isBegan && check) {
                onClick()
            }
            setNormalView()
            isBegan = false
        }
        if (type == TouchType.Cancelled) {
            isBegan = false
            setNormalView()
        }
        return !isBegan and chain
    }
}