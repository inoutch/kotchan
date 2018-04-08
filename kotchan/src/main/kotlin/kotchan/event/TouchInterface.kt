package kotchan.event

interface TouchInterface {
    fun touches(): List<Touch>
    fun touchesByOneCycle(index: Int): Touch?
}