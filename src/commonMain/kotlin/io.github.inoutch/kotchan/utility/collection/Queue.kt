package io.github.inoutch.kotchan.utility.collection

class Queue<T>(list: MutableList<T> = mutableListOf()) {

    val items: MutableList<T> = list

    fun isEmpty(): Boolean = this.items.isEmpty()

    fun count(): Int = this.items.count()

    override fun toString() = this.items.toString()

    fun enqueue(element: T) {
        this.items.add(element)
    }

    fun dequeue(): T? {
        return if (this.isEmpty()) {
            null
        } else {
            this.items.removeAt(0)
        }
    }

    fun peek(): T? {
        return this.items[0]
    }
}
