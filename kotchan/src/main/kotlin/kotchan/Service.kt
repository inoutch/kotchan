package kotchan

abstract class Service {
    protected val file = Engine.getInstance().file
    protected val windowSize = Engine.getInstance().windowSize
    protected val screenSize = Engine.getInstance().screenSize
}