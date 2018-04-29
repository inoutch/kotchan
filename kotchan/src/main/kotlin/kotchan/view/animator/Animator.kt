package kotchan.view.animator

import kotchan.view.animator.animate.Animate

class Animator {
    private val animates: MutableList<Animate> = mutableListOf()
    private val names: MutableMap<Animate, String> = mutableMapOf()

    fun run(animate: Animate) {
        animates.add(animate)
    }

    fun runJustOne(animate: Animate, name: String): Boolean {
        if (names.containsValue(name)) {
            return false
        }
        animates.add(animate)
        names[animate] = name
        return true
    }

    fun update(delta: Float) {
        val removeList = animates.filter { isFinished(it, delta) }.toList()
        removeList.forEach { names.remove(it) }
        animates.removeAll(removeList)
    }

    private fun isFinished(animate: Animate, delta: Float): Boolean {
        return if (animate.elapsedTime + delta < animate.duration) {
            animate.elapsedTime += delta
            animate.update(delta)
            false
        } else {
            animate.elapsedTime = animate.duration
            animate.done()
            animate.callback()
            true
        }
    }
}