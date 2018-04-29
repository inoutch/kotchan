package kotchan.controller.touch

interface TouchEmitter {
    // call each platforms
    fun onTouchesBegan(touchEvents: List<TouchEvent>)
    fun onTouchesMoved(touchEvents: List<TouchEvent>)
    fun onTouchesEnded(touchEvents: List<TouchEvent>)
    fun onTouchesCancelled()
}