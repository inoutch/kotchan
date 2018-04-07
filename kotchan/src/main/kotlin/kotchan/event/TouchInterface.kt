package kotchan.event

class TouchInterface(private val touchManager: TouchManager) {
    var touches: MutableList<TouchEvent> = mutableListOf()

    fun onTouchesBegan(touches: List<TouchEvent>) {
    }

    fun onTouchesMoved(touches: List<TouchEvent>) {

    }

    fun onTouchesEnded(touches: List<TouchEvent>) {

    }

    fun onTouchesCancelled() {

    }

    fun update() {

    }
}