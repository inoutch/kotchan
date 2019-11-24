package io.github.inoutch.kotchan.utility.collection

class FixedStack<T>(val maxSize: Int) : Collection<T> {

    private val stack = mutableListOf<T>()

    override val size: Int
        get() = stack.size

    operator fun get(index: Int): T = stack[stack.size - index - 1]

    fun clear() = stack.clear()

    fun push(item: T) {
        if (stack.size + 1 > maxSize) {
            stack.removeAt(0)
        }
        stack.add(item)
    }

    override fun contains(element: T): Boolean = stack.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = stack.containsAll(elements)

    override fun isEmpty(): Boolean = stack.isEmpty()

    override fun iterator(): Iterator<T> = stack.iterator()
}
