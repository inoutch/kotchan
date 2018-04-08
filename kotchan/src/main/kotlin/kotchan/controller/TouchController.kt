package kotchan.controller

interface TouchController {
    fun touches(): List<Touch>
    fun touchesByOneCycle(index: Int): Touch?
}